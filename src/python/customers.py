from collections import defaultdict

import random

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
