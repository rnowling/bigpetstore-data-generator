from collections import defaultdict

from items import ExhaustibleItemCategory

import parameters

import random

import products

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
        for category, model in self.inventory.iteritems():
            transaction_times.append(model.propose_transaction_time())
        return min(transaction_times)

    def item_category_probabilities(self):
        n = float(len(self.inventory)) + 1.0
        probabilities = [("stop", 1.0 / n)]
        for category in self.inventory:
            probabilities.append((category, 1.0 / n))
        return probabilities

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


class Transaction(object):
    def __init__(self, customer=None, trans_time=None, purchased_items=None, inventory=None):
        self.customer = customer
        self.trans_time = trans_time
        self.purchased_items = purchased_items
        self.inventory = inventory

    def __repr__(self):
        return "(%s, Time: %s, Purchased: %s, Inventory: %s)" % (self.customer, self.trans_time, len(self.purchased_items), self.inventory)


class TransactionPurchasesSimulator(object):
    def __init__(self, customer_state=None, products=None, trans_time=None):
        self.customer_state = customer_state
        self.products = products
        self.trans_time = trans_time

    def choose_category(self):
        category_probabilities = self.customer_state.item_category_probabilities()
        choosen_category, _ = random.choice(category_probabilities)
        return choosen_category

    def choose_item(self, category):
        items = self.products[category]
        return random.choice(items)

    def simulate(self):
        trans_items = []
        while True:
            category = self.choose_category()
            if category == "stop":
                break
            item = self.choose_item(category)
            self.customer_state.update_inventory(self.trans_time, item)
            trans_items.append(item)
        return trans_items


class TransactionSimulator(object):
    def __init__(self, customer_state=None, products=None):
        self.customer_state = customer_state
        self.products = products

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
        while last_trans_time < end_time:
            trans_time = self.next_transaction_time(last_trans_time)
            
            purchase_sim = TransactionPurchasesSimulator(customer_state=self.customer_state,
                                products=self.products,
                                trans_time=trans_time)
            purchased_items = purchase_sim.simulate()
            remaining = self.customer_state.get_inventory_amounts(trans_time)
            trans = Transaction(customer=self.customer_state.customer,
                        purchased_items=purchased_items,
                        trans_time=trans_time,
                        inventory=remaining)
            last_trans_time = trans_time
            yield trans

if __name__ == "__main__":
    from customers import CustomerGenerator

    generator = CustomerGenerator()
    customers = generator.generate(10)

    print "Customers"
    for c in customers:
        print c

    print

    print "States"
    states = []
    for c in customers:
        builder = CustomerStateBuilder(c, parameters)
        s = builder.build_state()
        states.append(s)
        print s

    print

    products_data = products.load_products()
    trans_sim = TransactionSimulator(customer_state=states[0], products=products_data)
    for trans in trans_sim.simulate(365.0 * 4.0):
        print trans

    