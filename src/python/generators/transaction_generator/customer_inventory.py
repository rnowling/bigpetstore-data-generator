import math
import random

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
            + math.sqrt(self.amount_used_variance * timestep) * r
        
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
    def __init__(self, item_category, num_pets):
        """
        daily_usage_rate is given in times/day -- used to determine when an item is used
        
        amount_used_average and amount_used_variance are given in units/day -- used to determine how
        much is used per usage.
        """        
        self.daily_usage_rate = item_category.daily_usage_rate
        self.amount_used_average = item_category.base_amount_used_average * num_pets
        self.amount_used_variance = item_category.base_amount_used_variance * num_pets
        
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


class CustomerInventory(object):
    def __init__(self, product_sims):
        self.product_sims = product_sims
        
    def record_purchase(self, time, item):
        item = dict(item)
        category = item["category"]
        amount = item["size"]
        sim = self.product_sims[category]
        sim.record_purchase(time, amount)

    def get_inventory_amounts(self, time):
        amounts = {}
        for category, sim in self.product_sims.iteritems():
            remaining_amount = sim.get_remaining_amount(time)
            amounts[category] = remaining_amount
        return amounts

    def get_exhaustion_times(self):
        times = {}
        for category, sim in self.product_sims.iteritems():
            exhaustion_time = sim.exhaustion_time()
            times[category] = exhaustion_time
        return times

class CustomerInventoryBuilder(object):
    def __init__(self, product_categories):
        self.product_categories = product_categories

    def build(self, customer_trans_params):
        product_sims = dict()

        for category, model in self.product_categories.iteritems():
            num_pets = 0
            for species in model.species:
                num_pets += customer_trans_params.pet_counts[species]
            if num_pets > 0:
                product_sims[category] = \
                    ItemCategorySimulation(model, num_pets)

        return CustomerInventory(product_sims)
