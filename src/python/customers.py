from collections import defaultdict

import random

from item_simulations import ExhaustibleItemCategorySimulation	

class Customer(object):
	def __init__(self):
		self.name = None
		self.location = None
		self.pets = {
						"dog" : 0,
						"cat" : 0
					}

		self.product_repetition_weight = None

	def __repr__(self):
		return "(%s, %s dogs, %s cats)" % (self.name, self.pets["dog"], self.pets["cat"])


class CustomerGenerator(object):
	def generate(self, n):
		customers = list()
		for i in xrange(n):
			customer = Customer()
			customer.name = "Customer " + str(i)
			
			num_pets = random.randint(1, 10)
			num_dogs = random.randint(0, num_pets)
			num_cats = num_pets - num_dogs
			
			customer.pets["dog"] = num_dogs
			customer.pets["cat"] = num_cats
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

