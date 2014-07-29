from markovmodel import MarkovModelBuilder
from item_simulations import ItemCategorySimulation

import random

class ItemCategoryMarkovModelBuilder(object):
    def __init__(self, item_category=None, customer=None):
        self.item_category = item_category
        self.customer = customer

    def _normalize_field_weights(self):
        weight_sum = sum(self.field_weights.itervalues())

        for field, weight in list(self.field_weights.iteritems()):
            self.field_weights[field] = weight / weight_sum

    def _generate_transition_parameters(self):
        self.field_weights = dict()
        self.field_similarity_weights = dict()
        for field in self.item_category.fields:
            avg = random.choice([0.15, 0.85])
            self.field_weights[field] = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))
            avg = random.choice([0.15, 0.85])
            self.field_similarity_weights[field] = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))
        avg = random.choice([0.25, 0.75])
        self.loopback_weight = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))

    def similarity_weight(self, rec1, rec2):
        weight = 0.0
        for field in self.item_category.fields:
            if rec1[field] == rec2[field]:
                weight += self.field_weights[field] * self.field_similarity_weights[field]
            else:
                weight += self.field_weights[field] * (1.0 - self.field_similarity_weights[field])
        return weight

    def create_markov_model(self):
        self._generate_transition_parameters()
        self._normalize_field_weights()

        builder = MarkovModelBuilder()

        for idx, rec in enumerate(self.item_category.items):
            builder.add_state(tuple(rec.items()))
            weight_sum = 0.0
            for other_rec in self.item_category.items:
                if rec != other_rec:
                    weight_sum += self.similarity_weight(rec, other_rec)

            for other_rec in self.item_category.items:
                if rec == other_rec:
                    builder.add_edge_weight(tuple(rec.items()),
                                            tuple(other_rec.items()),
                                            self.loopback_weight)
                else:
                    weight = (1.0 - self.loopback_weight) * \
                        self.similarity_weight(rec, other_rec) / weight_sum
                    builder.add_edge_weight(tuple(rec.items()),
                                            tuple(other_rec.items()),
                                            weight)               

        return builder.build_msm()


class CustomerState(object):
    def __init__(self, item_categories=None, customer=None):
        self.customer = customer
        
        self.item_usage_sims = dict()
        self.item_purchase_msms = dict()
        for category, model in item_categories.iteritems():
            num_pets = 0
            for species in model.species:
                num_pets += customer.pets[species]
            if num_pets > 0:
                self.item_usage_sims[category] = \
                    ItemCategorySimulation(item_category=model,
                    customer=customer)
                builder = ItemCategoryMarkovModelBuilder(item_category=model,
                                               customer=customer)
                self.item_purchase_msms[category] = builder.create_markov_model()


    def choose_item(self, category):
        item = self.item_purchase_msms[category].progress_state()
        return item

    def propose_transaction_time(self):
        transaction_times = []
        for category, sim in self.item_usage_sims.iteritems():
            time = sim.propose_transaction_time()
            transaction_times.append(time)
        
        return min(transaction_times)

    def item_category_weights(self, current_time):
        weights = []
        for category_name, sim in self.item_usage_sims.iteritems():
            weights.append((category_name, sim.purchase_weight(current_time)))
        return weights

    def update_inventory(self, time, category, item):
        item = dict(item)
        amount = item["quantity"]
        sim = self.item_usage_sims[category]
        sim.record_purchase(time, amount)

    def get_inventory_amounts(self, time):
        amounts = {}
        for category, sim in self.item_usage_sims.iteritems():
            remaining_amount = sim.get_remaining_amount(time)
            amounts[category] = remaining_amount
        return amounts

    def __repr__(self):
        return "(%s, %s dogs, %s cats)" % (self.customer.name, self.customer.pets["dog"], self.customer.pets["cat"])

if __name__ == "__main__":
    from products import load_products_json
    from customers import CustomerGenerator

    item_categories = load_products_json()

    customer = CustomerGenerator().generate(1)[0]

    customer_sim = CustomerState(item_categories=item_categories,
        customer=customer)

