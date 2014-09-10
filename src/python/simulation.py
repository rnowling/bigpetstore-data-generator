import simulation_parameters as sim_param

from readers import load_names
from readers import load_products
from readers import load_zipcode_data

from generators.store_generator import StoreGenerator
from generators.customer_generator import CustomerGenerator
from generators.purchasing_profile_generator import PurchasingProfileGenerator
from generators.transaction_generator import TransactionGenerator

from writers import CustomerWriter
from writers import StoreWriter
from writers import TransactionItemWriter
from writers import TransactionWriter

class Simulator(object):
    def load_data(self):
        self.item_categories = load_products(sim_param.PRODUCTS_FILE)
        self.zipcode_objs = load_zipcode_data(**sim_param.ZIPCODE_DATA_FILES)
        self.names = load_names(sim_param.NAMEDB_FILE)

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
                                      names=self.names)
        self.customers = []
        for i in xrange(num):
            customer = generator.generate()
            self.customers.append(customer)

    def generate_transactions(self, end_time=None):
        profile_generator = PurchasingProfileGenerator(self.item_categories)

        trans_sim = TransactionGenerator(stores=self.stores,
                                         product_categories=self.item_categories)

        for customer in self.customers:
            profile = profile_generator.generate()
            for trans in trans_sim.simulate(customer, profile, end_time):
                yield trans


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
