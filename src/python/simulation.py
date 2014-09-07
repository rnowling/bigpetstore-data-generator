from customers import CustomerGenerator
from customer_simulation import CustomerState
import simulation_parameters as sim_param
from transactions import TransactionSimulator

from readers import load_names
from readers import load_products
from readers import load_zipcode_data

from generators.store_generator import StoreGenerator
from generators.customer_generator import CustomerGenerator

class Simulator(object):
    def __init__(self):
        pass

    def load_data(self):
        self.item_categories = load_products(sim_param.PRODUCTS_FILE)
        self.zipcode_objs = load_zipcode_data(**sim_param.ZIPCODE_DATA_FILES)
        self.first_names, self.last_names = load_names(sim_param.NAMEDB_FILE)

    def generate_stores(self, num=None):
        self.stores = []
        generator = StoreGenerator(zipcode_objs=self.zipcode_objs,
                                   income_scaling_factor=sim_param.STORE_INCOME_SCALING_FACTOR)
        for i in xrange(num):
            store = generator.generate()
            self.stores.append(store)

    def generate_customers(self, num=None):
        generator = CustomerGenerator(zipcode_objs=self.zipcode_objs,
                                      stores=self.stores,
                                      first_names=self.first_names,
                                      last_names=self.last_names)
        self.customers = []
        for i in xrange(num):
            customer = generator.generate()
            self.customers.append(customer)

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
