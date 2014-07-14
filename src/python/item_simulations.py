import random

import numpy as np

from markovmodel import MarkovModelBuilder

class ItemCategoryMarkovModelBuilder(object):
	def __init__(self, item_category=None, customer=None):
		self.item_category = item_category
		self.customer = customer

	def _normalize_field_weights(self, field_weights):
		weight_sum = 0.0
		for field, weight in field_weights.iteritems():
			weight_sum += weight

		for field, weight in list(field_weights.iteritems()):
			field_weights[field] = weight / weight_sum

		return field_weights

	def _generate_transition_parameters(self):
		field_weights = dict()
		field_similarity_weights = dict()
		for field in self.item_category.fields:
			field_weights[field] = 1.0 # random.uniform(0.0, 1.0)
			field_similarity_weights[field] = 1.0 # random.uniform(0.0, 1.0)
		loopback_weight = 0.9

		return field_weights, field_similarity_weights, loopback_weight

	def create_markov_model(self):
		field_weights, field_similarity_weights, loopback_weight = \
			self._generate_transition_parameters()
		field_weights = self._normalize_field_weights(field_weights)

		builder = MarkovModelBuilder()
		for rec in self.item_category.items:
			builder.add_state(tuple(rec.items()))
			for other_rec in self.item_category.items:
				weight = 0.0
				if rec == other_rec:
					weight = loopback_weight
				else:
					for field in self.item_category.fields:
						if rec[field] == other_rec[field]:
							weight += field_weights[field] * field_similarity_weights[field]
						else:
							weight += field_weights[field] * (1.0 - field_similarity_weights[field])
					weight = (1.0 - loopback_weight) * weight
				builder.add_edge_weight(tuple(rec.items()), tuple(other_rec.items()), weight)

		return builder.build_msm()

class ExhaustibleItemCategoryUsageSimulation(object):
	def __init__(self, initial_amount=None, initial_time=None, daily_usage_rate=None, amount_used_average=None, amount_used_variance=None):
		"""
		daily_usage_rate is given in times/day -- used to determine when an item is used
		
		amount_used_average and amount_used_variance are given in units/day -- used to determine how
		much is used per usage.
		"""
		
		self.daily_usage_rate = daily_usage_rate
		self.amount_used_average = amount_used_average
		self.amount_used_variance = amount_used_variance
		
		
		self.trajectory = [(initial_time, initial_amount)]
		
		self.time = initial_time
		self.remaining_amount = initial_amount
		
		self.ran_simulation = False
		
	def _step(self):
		"""
		Simulate 1 step of usage.
		
		da/dt = -R(amount_used_average, amount_used_variance)
		
		timestep is determined from exponential distribution:
		f = \lambda \exp (-\lambda t) where \lambda = 1.0 / usage_rate
		\Delta t sampled from f
		
		Returns time after 1 step and remaining amount
		"""
		
		# given in days since last usage
		timestep = random.expovariate(1.0 / self.daily_usage_rate)
		
		# Might be more realistic to model usage
		# in units/time used since items are used
		# in discrete rather than continuous
		# quantities
		
		# given in units/day
		usage_rate = random.normalvariate(self.amount_used_average, self.amount_used_variance)
		
		# can't use a negative amount :)
		if usage_rate < 0.0:
			usage_rate = 0.0
		
		# given in units
		usage_amount = usage_rate * np.sqrt(timestep)
		
		self.remaining_amount -= min(usage_amount, self.remaining_amount)
		
		self.time += timestep
		
		self.trajectory.append((self.time, self.remaining_amount))
		
	def simulate(self):
		while self.remaining_amount > 0.0:
			self._step()
		self.ran_simulation = True
			
	def exhaustion_time(self):
		if not self.ran_simulation:
			raise Exception, "Cannot return exhaustion time before running simulation"
		return self.trajectory[-1][0]
	
	def amount_at_time(self, time):
		"""
		Find amount remaining at given time.
		"""
		previous_t, previous_amount = self.trajectory[0]
		for t, amount in self.trajectory[1:]:
			if t > time:
				break
			previous_t = t
			previous_amount = amount
		return previous_amount


class ExhaustibleItemCategorySimulation(object):
	def __init__(self, item_category=None, customer=None):
		"""
		daily_usage_rate is given in times/day -- used to determine when an item is used
		
		amount_used_average and amount_used_variance are given in units/day -- used to determine how
		much is used per usage.
		"""
		
		num_pets = 0.0
		for species in item_category.species:
			num_pets += float(customer.pets[species])

		self.daily_usage_rate = item_category.daily_usage_rate
		self.amount_used_average = item_category.base_amount_used_average * num_pets
		self.amount_used_variance = item_category.base_amount_used_variance * num_pets
		
		self.transaction_trigger_rate = item_category.transaction_trigger_rate
		self.transaction_purchase_rate = item_category.transaction_purchase_rate
		self.sim = None
		

		self.purchase_model = ItemCategoryMarkovModelBuilder(item_category=item_category,
			customer=customer).create_markov_model()
				
	def record_purchase(self, purchase_time, purchased_amount):
		"""
		Increase current amount, from a purchase.
		
		purchase_time -- given in seconds since start of model
		
		purchased_amount -- given in units
		"""
		
		total_amount = purchased_amount
		if self.sim != None:
			total_amount += self.sim.amount_at_time(purchase_time)
		
		self.sim = ExhaustibleItemCategoryUsageSimulation(initial_amount=total_amount,
											 initial_time=purchase_time,
											 daily_usage_rate=self.daily_usage_rate,
											 amount_used_average=self.amount_used_average,
											 amount_used_variance=self.amount_used_variance)
		
		self.sim.simulate()

	def exhaustion_time(self):
		exhaustion_time = 0.0
		if self.sim != None:
			exhaustion_time = self.sim.exhaustion_time()
		return exhaustion_time

	def get_remaining_amount(self, time):
		if self.sim == None:
			return 0.0
		return self.sim.amount_at_time(time)

	def purchase_weight(self, time):
		remaining_time = max(self.exhaustion_time() - time, 0.0)
		lambd = 1.0 / self.transaction_purchase_rate
		return lambd * np.exp(-lambd * remaining_time)
		
	def propose_transaction_time(self):
		time_until_transaction = random.expovariate(1.0 / self.transaction_trigger_rate)
		transaction_time = max(self.exhaustion_time() - time_until_transaction, 0.0)
		return transaction_time

	def choose_item_for_purchase(self):
		return self.purchase_model.progress_state()


if __name__ == "__main__":
	sim = ExhaustibleItemCategoryUsageSimulation(initial_amount=30.0, initial_time=0.0, daily_usage_rate=1.0,
		amount_used_average=0.5, amount_used_variance=0.2)
	sim.simulate()

	for time, amount in sim.trajectory:
		print time, amount

	from products import load_products_json
	from customers import CustomerGenerator

	item_categories = load_products_json()

	customer = CustomerGenerator().generate(1)[0]
	print 
	for item_category in item_categories.itervalues():
		sim = ExhaustibleItemCategorySimulation(item_category=item_category, customer=customer)
		for i in xrange(10):
			print sim.choose_item_for_purchase()
