from collections import defaultdict

import random

import parameters

from items import ExhaustibleItemCategory

class Customer(object):
	def __init__(self):
		self.name = None
		self.location = None
		self.pets = {
					"dog" : 0,
					"cat" : 0
				}

	def __repr__(self):
		return "(%s, %s dogs, %s cats)" % (self.name, self.pets["dog"], self.pets["cat"])

class CustomerStateBuilder(object):
	def __init__(self, customer, parameters):
		self.customer = customer
		self.parameters = parameters

	def calculate_rates(self):
		rates = defaultdict(dict)
		for category, cat_params in self.parameters.item_categories.iteritems():
			species = cat_params["species"]
			if self.customer.pets[species] == 0:
				continue
			rates[category]["daily_usage_rate"] = cat_params["daily_usage_rate"]
			rates[category]["transaction_trigger_rate"] = cat_params["transaction_trigger_rate"]
			rates[category]["transaction_purchase_rate"] = cat_params["transaction_purchase_rate"]
			rates[category]["amount_used_average"] = cat_params["base_amount_used_average"] * float(self.customer.pets[species])
			rates[category]["amount_used_variance"] = cat_params["base_amount_used_variance"] * float(self.customer.pets[species])
		return rates

	def build_state(self):
		inventory = self.calculate_rates()
		return CustomerState(self.customer, inventory)

class CustomerState(object):
	def __init__(self, customer, inventory):
		self.customer = customer
		
		self.inventory = {}
		for category, rates in inventory.iteritems():
			self.inventory[category] = ExhaustibleItemCategory(**rates)
		
		self.transactions = {}

	def propose_transaction_time(self):
		transaction_times = []
		print "Proposed transaction times: "
		for category, model in self.inventory.iteritems():
			time = model.propose_transaction_time()
			print "\t%s, %s, %s" % (category, time, model.exhaustion_time())
			transaction_times.append(time)
		print
		
		return min(transaction_times)

	def item_category_weights(self, current_time):
		n = float(len(self.inventory))
		weights = []
		for category_name, model in self.inventory.iteritems():
			weights.append((category_name, model.purchase_weight(current_time)))
		return weights

	def update_inventory(self, time, item):
		category = item["category"]
		amount = item["size"]
		model = self.inventory[category]
		model.purchase(time, amount)

	def get_inventory_amounts(self, time):
		amounts = {}
		for category, model in self.inventory.iteritems():
			remaining_amount = model.get_remaining_amount(time)
			amounts[category] = remaining_amount
		return amounts


	def __repr__(self):
		return "(%s, %s dogs, %s cats, " % (self.customer.name, self.customer.pets["dog"], self.customer.pets["cat"]) + repr(self.inventory) + ")"


class CustomerGenerator(object):
	def generate(self, n):
		customers = list()
		for i in xrange(n):
			customer = Customer()
			customer.name = "Customer " + str(i)
			customer.pets["dog"] = random.randint(1, 4)
			customer.pets["cat"] = random.randint(1, 4)
			customers.append(customer)
		return customers

if __name__ == "__main__":
	generator = CustomerGenerator()
	customers = generator.generate(10)

	print "Customers"
	for c in customers:
		print c

	print

	print "States"
	for c in customers:
		builder = CustomerStateBuilder(c, parameters)
		s = builder.build_state()
		print s

	print 

	import products
	products_list = products.load_products()
	item = random.choice(products_list["Kitty Litter"])

	state = s
	state.update_inventory(0.0, item)

	print item
	print state.inventory["Kitty Litter"].exhaustion_time()
	print state.inventory["Kitty Litter"].propose_transaction_time()
	print

	for time, amount in state.inventory["Kitty Litter"].sim.trajectory:
		print time, amount

