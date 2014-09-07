import random

import numpy as np

class ItemCategoryUsageSimulation(object):
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
        timestep = random.expovariate(self.daily_usage_rate)
        

        r = random.normalvariate(0.0, 1.0)
        
        # given in units/day
        usage_amount = self.amount_used_average * timestep \
            + np.sqrt(self.amount_used_variance * timestep) * r
        
        # can't use a negative amount :)
        if usage_amount < 0.0:
            usage_amount = 0.0
                
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


class ItemCategorySimulation(object):
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

        self.average_transaction_trigger_time = customer.average_transaction_trigger_time
        self.average_purchase_trigger_time = customer.average_purchase_trigger_time
        
        self.sim = None
                        
    def record_purchase(self, purchase_time, purchased_amount):
        """
        Increase current amount, from a purchase.
        
        purchase_time -- given in seconds since start of model
        
        purchased_amount -- given in units
        """
        
        total_amount = purchased_amount
        if self.sim != None:
            total_amount += self.sim.amount_at_time(purchase_time)
        
        self.sim = ItemCategoryUsageSimulation(initial_amount=total_amount,
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
        lambd = 1.0 / self.average_purchase_trigger_time
        return lambd * np.exp(-lambd * remaining_time)
        
    def propose_transaction_time(self):
        lambd = 1.0 / self.average_transaction_trigger_time
        time_until_transaction = random.expovariate(lambd)
        transaction_time = max(self.exhaustion_time() - time_until_transaction, 0.0)
        return transaction_time

    def choose_item_for_purchase(self):
        return self.purchase_model.progress_state()


