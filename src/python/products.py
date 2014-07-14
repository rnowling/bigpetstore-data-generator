from collections import defaultdict

import random

import json

class ItemCategory(object):
	def __init__(self, category=None, fields=None, items=None, \
			species=None, trigger_transaction=False,
			daily_usage_rate=None, base_amount_used_average=None,
			base_amount_used_variance=None,
			transaction_trigger_rate=None,
			transaction_purchase_rate=None):
	
		self.category_label = category
		self.fields = fields
		self.items = items
		self.species = species
		self.trigger_transaction = trigger_transaction
		self.daily_usage_rate = daily_usage_rate
		self.base_amount_used_average = base_amount_used_average
		self.base_amount_used_variance = base_amount_used_average
		self.transaction_trigger_rate = transaction_trigger_rate
		self.transaction_purchase_rate = transaction_purchase_rate



def load_products_json():
	category_fl = open("product_categories.json")
	product_categories = json.load(category_fl)
	category_fl.close()

	item_category_objects = dict()
	for category in product_categories:
		item_category_objects[category["category"]] = ItemCategory(**category)

	return item_category_objects

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

	#markov_model = product_cateogry_objects["dry dog food"].create_markov_model()

	#for i in xrange(20):
	#	print markov_model.progress_state()
