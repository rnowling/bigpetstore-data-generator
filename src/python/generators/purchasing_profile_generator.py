from algorithms.markovmodel import MarkovModelBuilder
from algorithms.samplers import BoundedMultiModalGaussianSampler

from datamodels.simulation_models import PurchasingProfile

import simulation_parameters as sim_params

class PurchasingProfileBuilder(object):
    def __init__(self):
        self.profiles = dict()

    def add_profile(self, product_category, markov_model):
        self.profiles[product_category] = markov_model
    
    def build(self):
        return PurchasingProfile(self.profiles.copy())

class ProductCategoryMarkovModelGenerator(object):
    def __init__(self, product_category):
        self.product_category = product_category

        self.field_weight_sampler = BoundedMultiModalGaussianSampler(
            sim_params.PRODUCT_MSM_FIELD_WEIGHT_GAUSSIANS,
            bounds=sim_params.PRODUCT_MSM_FIELD_WEIGHT_BOUNDS)

        self.field_sim_weight_sampler = BoundedMultiModalGaussianSampler(
            sim_params.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_GAUSSIANS,
            bounds=sim_params.PRODUCT_MSM_FIELD_SIMILARITY_WEIGHT_BOUNDS)

        self.loopback_weight_sampler = BoundedMultiModalGaussianSampler(
            sim_params.PRODUCT_MSM_LOOPBACK_WEIGHT_GAUSSIANS,
            bounds=sim_params.PRODUCT_MSM_LOOPBACK_WEIGHT_BOUNDS)

    def _normalize_field_weights(self):
        weight_sum = sum(self.field_weights.itervalues())

        for field, weight in list(self.field_weights.iteritems()):
            self.field_weights[field] = weight / weight_sum

    def _generate_transition_parameters(self):
        self.field_weights = dict()
        self.field_similarity_weights = dict()
        for field in self.product_category.fields:
            self.field_weights[field] = self.field_weight_sampler.sample()
            self.field_similarity_weights[field] = self.field_sim_weight_sampler.sample()
        self.loopback_weight = self.loopback_weight_sampler.sample()

    def _similarity_weight(self, rec1, rec2):
        weight = 0.0
        for field in self.product_category.fields:
            if rec1[field] == rec2[field]:
                weight += self.field_weights[field] * self.field_similarity_weights[field]
            else:
                weight += self.field_weights[field] * (1.0 - self.field_similarity_weights[field])
        return weight

    def generate(self):
        self._generate_transition_parameters()
        self._normalize_field_weights()

        builder = MarkovModelBuilder()

        for rec in self.product_category.items:
            builder.add_start_state(tuple(rec.items()), 1.0)
            weight_sum = 0.0
            for other_rec in self.product_category.items:
                if rec != other_rec:
                    weight_sum += self._similarity_weight(rec, other_rec)

            for other_rec in self.product_category.items:
                weight = 0.0
                if rec != other_rec:
                    weight = (1.0 - self.loopback_weight) * self._similarity_weight(rec, other_rec) / weight_sum
                else:
                    weight = self.loopback_weight
                builder.add_edge_weight(tuple(rec.items()), tuple(other_rec.items()), weight)

        return builder.build_msm()

class PurchasingProfileGenerator(object):
    def __init__(self, product_categories):
        self.product_categories = product_categories

    def generate(self):
        profile_builder = PurchasingProfileBuilder()
        for label, record in self.product_categories.iteritems():
            msm_generator = ProductCategoryMarkovModelGenerator(record)
            msm = msm_generator.generate()
            profile_builder.add_profile(label, msm)
        return profile_builder.build()
