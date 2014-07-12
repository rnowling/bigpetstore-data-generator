from collections import defaultdict

import random

from markovmodel import MarkovModelBuilder

import json

class ProductCategory(object):
	def __init__(self, fields, records):
		self.fields = fields
		self.records = records

	def normalize_field_weights(self, field_weights):
		weight_sum = 0.0
		for field, weight in field_weights.iteritems():
			weight_sum += weight

		for field, weight in list(field_weights.iteritems()):
			field_weights[field] = weight / weight_sum

		return field_weights

	def generate_transition_parameters(self):
		field_weights = dict()
		field_similarity_weights = dict()
		for field in self.fields:
			field_weights[field] = 1.0 # random.uniform(0.0, 1.0)
			field_similarity_weights[field] = 1.0 # random.uniform(0.0, 1.0)
		loopback_weight = 0.9

		return field_weights, field_similarity_weights, loopback_weight

	def create_markov_model(self):
		field_weights, field_similarity_weights, loopback_weight = \
			self.generate_transition_parameters()
		field_weights = self.normalize_field_weights(field_weights)

		builder = MarkovModelBuilder()
		for rec in self.records:
			builder.add_state(tuple(rec.items()))
			for other_rec in self.records:
				weight = 0.0
				if rec == other_rec:
					weight = loopback_weight
				else:
					for field in self.fields:
						if rec[field] == other_rec[field]:
							weight += field_weights[field] * field_similarity_weights[field]
						else:
							weight += field_weights[field] * (1.0 - field_similarity_weights[field])
					weight = (1.0 - loopback_weight) * weight
				builder.add_edge_weight(tuple(rec.items()), tuple(other_rec.items()), weight)

		return builder.build_msm()

def product_category_factory(fields, records):
	return ProductCategory(fields, records)

def load_products_json():
	category_fl = open("product_categories.json")
	category_descriptions = json.load(category_fl)
	category_fl.close()

	products_fl = open("products.json")
	products = json.load(products_fl)
	products_fl.close()

	products_by_category = defaultdict(list)
	for product in products:
		category = product["category"]
		products_by_category[category].append(product)

	product_cateogry_objects = dict()
	for category in category_descriptions:
		product_cateogry_objects[category["category"]] = ProductCategory(category["fields"], products_by_category[category["category"]])

	return product_cateogry_objects

def load_products():
	products = defaultdict(list)
	fl = open("products.csv")

	# skip header
	next(fl)

	for ln in fl:
		cols = ln.strip().split(",")
		category = cols[0]
		item = {
			"category" : cols[0],
			"brand" : cols[1],
			"flavor" : cols[2],
			"size" : float(cols[3]),
			"per_unit_cost" : float(cols[4])
			}
		products[category].append(item)

	fl.close()

	return products

if __name__ == "__main__":
	product_cateogry_objects = load_products_json()

	markov_model = product_cateogry_objects["dry dog food"].create_markov_model()

	for i in xrange(20):
		print markov_model.progress_state()
