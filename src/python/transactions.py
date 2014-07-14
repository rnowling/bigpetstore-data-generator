from collections import defaultdict

import random

from samplers import RouletteWheelSampler

class Transaction(object):
	def __init__(self, customer=None, trans_time=None, purchased_items=None, inventory_before=None, inventory_after=None):
		self.customer = customer
		self.trans_time = trans_time
		self.purchased_items = purchased_items
		self.inventory_before = inventory_before
		self.inventory_after = inventory_after

	def __repr__(self):
		return "(%s, Time: %s, Purchased: %s\n\tInventory Before: %s\n\tInventory After: %s)" % (self.customer,
			self.trans_time, len(self.purchased_items), self.inventory_before,
				self.inventory_after)


class TransactionPurchasesSimulator(object):
	def __init__(self, customer_state=None, trans_time=None):
		self.customer_state = customer_state
		self.trans_time = trans_time
		self.purchases = 0

	def choose_category(self):
		category_weights = self.customer_state.item_category_weights(self.trans_time)

		if self.purchases != 0:
			category_weights.append(("stop", 0.1))

		weight_sum = 0.0
		for category, weight in category_weights:
			print category, weight
			weight_sum += weight

		category_probabilities = []
		for category, weight in category_weights:
			category_probabilities.append((category, weight / weight_sum))

		sampler = RouletteWheelSampler(category_probabilities)
		
		return sampler.sample()

	def simulate(self):
		trans_items = []
		while True:
			category = self.choose_category()
			if category == "stop":
				break
			item = self.customer_state.choose_item(category)
			self.customer_state.update_inventory(self.trans_time, item)
			self.purchases += 1
			trans_items.append(item)
		return trans_items


class TransactionSimulator(object):
	def __init__(self, customer_state=None):
		self.customer_state = customer_state

	def trans_time_probability(self, proposed_trans_time, last_trans_time):
		if proposed_trans_time >= last_trans_time:
			return 1.0
		else:
			return 0.0

	def next_transaction_time(self, last_trans_time):
		while True:
			proposed_time = self.customer_state.propose_transaction_time()
			prob = self.trans_time_probability(proposed_time, last_trans_time)
			r = random.random()
			if r < prob:
				return proposed_time
	
	def simulate(self, end_time):
		last_trans_time = 0.0
		while True:
			trans_time = self.next_transaction_time(last_trans_time)

			if trans_time > end_time:
				break
			
			purchase_sim = TransactionPurchasesSimulator(customer_state=self.customer_state,
								trans_time=trans_time)
			remaining_before = self.customer_state.get_inventory_amounts(trans_time)
			purchased_items = purchase_sim.simulate()
			remaining_after = self.customer_state.get_inventory_amounts(trans_time)
			trans = Transaction(customer=self.customer_state.customer,
						purchased_items=purchased_items,
						trans_time=trans_time,
						inventory_before=remaining_before,
						inventory_after=remaining_after)
			last_trans_time = trans_time
			yield trans

	