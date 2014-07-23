from customers import CustomerGenerator
from customers import load_names
from customer_simulation import CustomerState
import simulation_parameters as sim_param
from stores import StoreGenerator
import products
from transactions import TransactionSimulator
from zipcodes import load_zipcode_data


class Simulator(object):
    def __init__(self):
        pass

    def load_data(self):
        self.item_categories = products.load_products_json()
        self.zipcode_objs = load_zipcode_data()
        self.first_names, self.last_names = load_names()

    def generate_stores(self, num=None):
        generator = StoreGenerator(zipcode_objs=self.zipcode_objs,
                                   income_scaling_factor=sim_param.STORE_INCOME_SCALING_FACTOR)
        self.stores = generator.generate(n=num)

    def generate_customers(self, num=None):
        generator = CustomerGenerator(zipcode_objs=self.zipcode_objs,
                                      stores=self.stores,
                                      first_names=self.first_names,
                                      last_names=self.last_names)
        self.customers = generator.generate(num)

    def generate_transactions(self, end_time=None):
        for customer in self.customers:
            state = CustomerState(item_categories=self.item_categories,
                    customer=customer)
            trans_sim = TransactionSimulator(stores=self.stores,
                                             customer_state=state,
                                             item_categories=self.item_categories)
            for trans in trans_sim.simulate(end_time):
                yield trans

class TransactionItemWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, trans):
        for item in trans.purchased_items:
            item = dict(item)
            if "food" in item["category"]:
                item_str = "%s:%s:%s:%s" % \
                    (item["category"], item["brand"], item["flavor"], 
                     item["size"])
            elif "poop bags" == item["category"]:
                item_str = "%s:%s:%s:%s" % \
                    (item["category"], item["brand"], item["color"], 
                     item["size"])
            else:
                item_str = "%s:%s:%s" % \
                    (item["category"], item["brand"], item["size"])

            self.fl.write("%s,%s\n" % (trans.transaction_id(),
                                  item_str))
    def close(self):
        self.fl.close()
    

class CustomerWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, customer):
        string = "%s,%s,%s\n" % (customer.id,
                                 customer.name, 
                                 customer.location)
                     
        self.fl.write(string)

    def close(self):
        self.fl.close()

class StoreWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, store):
        string = "%s,%s\n" % (store.id, store.zipcode)
        self.fl.write(string)

    def close(self):
        self.fl.close()


class TransactionWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, trans):
            values = [
                trans.transaction_id(),
                trans.store.id,
                trans.customer.id,
                trans.trans_time,
                ]
            string = ",".join(map(str, values)) + "\n"
            self.fl.write(string)

    def close(self):
        self.fl.close()

def driver():
    sim = Simulator()
    item_writer = TransactionItemWriter(filename="transaction_items.txt")
    trans_writer = TransactionWriter(filename="transactions.txt")
    store_writer = StoreWriter(filename="stores.txt")
    customer_writer = CustomerWriter(filename="customers.txt")

    print "Loading data..."
    sim.load_data()
    
    print "Generating stores..."
    sim.generate_stores(num=10)

    for store in sim.stores:
        store_writer.append(store)
    store_writer.close()

    print "Generating customers..."
    sim.generate_customers(num=100)

    for customer in sim.customers:
        customer_writer.append(customer)
    customer_writer.close()

    print "Generating transactions..."
    for trans in sim.generate_transactions(end_time=365.0*5.0):
        trans_writer.append(trans)
        item_writer.append(trans)

    print

    trans_writer.close()
    item_writer.close()

if __name__ == "__main__":
    driver()
