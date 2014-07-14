import products

from customers import CustomerGenerator
from customer_simulation import CustomerState

from transactions import TransactionSimulator


class Simulator(object):
	def __init__(self, num_customers=None):
		self.customers = CustomerGenerator().generate(num_customers)
		self.item_categories = products.load_products_json()

	def simulate(self, end_time=None):
		for customer in self.customers:
			state = CustomerState(item_categories=self.item_categories,
				customer=customer)
			trans_sim = TransactionSimulator(customer_state=state)
			for trans in trans_sim.simulate(end_time):
				yield trans



if __name__ == "__main__":
	sim = Simulator(num_customers=5)
	for trans in sim.simulate(120.0):
		print trans
