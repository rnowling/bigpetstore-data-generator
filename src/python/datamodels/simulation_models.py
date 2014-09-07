from generators.transaction_generator.item_simulations import ItemCategorySimulation

class CustomerState(object):
    def __init__(self, item_categories=None, customer=None):
        self.customer = customer
        
        self.item_sims = dict()
        for category, model in item_categories.iteritems():
            num_pets = 0
            for species in model.species:
                num_pets += customer.pets[species]
            if num_pets > 0:
                self.item_sims[category] = ItemCategorySimulation(item_category=model,
                    customer=customer)

    def propose_transaction_time(self):
        transaction_times = []
        for category, sim in self.item_sims.iteritems():
            time = sim.propose_transaction_time()
            transaction_times.append(time)
        
        return min(transaction_times)

    def item_category_weights(self, current_time):
        weights = []
        for category_name, sim in self.item_sims.iteritems():
            weights.append((category_name, sim.purchase_weight(current_time)))
        return weights

    def update_inventory(self, time, item):
        item = dict(item)
        category = item["category"]
        amount = item["size"]
        sim = self.item_sims[category]
        sim.record_purchase(time, amount)

    def get_inventory_amounts(self, time):
        amounts = {}
        for category, sim in self.item_sims.iteritems():
            remaining_amount = sim.get_remaining_amount(time)
            amounts[category] = remaining_amount
        return amounts

    def __repr__(self):
        return "(%s, %s dogs, %s cats)" % (self.customer.name, self.customer.pets["dog"], self.customer.pets["cat"])

class PurchasingProfile(object):
    def __init__(self):
        self.product_category_profiles = dict()

    def add_profile(self, product_category, markov_model):
        self.product_category_profiles[product_category] = markov_model

    def get_profile(self, product_category):
        return self.product_category_profiles[product_category]

    def get_product_categories(self):
        return self.product_category_profiles.keys()
