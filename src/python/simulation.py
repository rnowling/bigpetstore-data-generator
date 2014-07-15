from customers import CustomerGenerator
from customer_simulation import CustomerState
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

    def generate_stores(self, num=None):
        generator = StoreGenerator(zipcode_objs=self.zipcode_objs,
                                   income_scaling_factor=100.0)
        self.stores = generator.generate(n=num)

    def generate_customers(self, num=None):
        generator = CustomerGenerator(zipcode_objs=self.zipcode_objs,
                                      stores=self.stores)
        self.customers = generator.generate(num)

    def generate_transactions(self, end_time=None):
        for customer in self.customers:
            state = CustomerState(item_categories=self.item_categories,
                    customer=customer)
            trans_sim = TransactionSimulator(customer_state=state,
                                             item_categories=self.item_categories)
            for trans in trans_sim.simulate(end_time):
                yield trans

class TransactionWriter(object):
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
            

            values = [
                trans.customer.name,
                trans.customer.location,
                trans.trans_time,
                item_str
                ]
            string = ",".join(map(str, values)) + "\n"
            self.fl.write(string)

    def close(self):
        self.fl.close()

if __name__ == "__main__":
    sim = Simulator()
    trans_writer = TransactionWriter(filename="transactions.txt")

    print "Loading data..."
    sim.load_data()
    
    print "Generating stores..."
    sim.generate_stores(num=100)

    print "Generating customers..."
    sim.generate_customers(num=10)

    print "Generating transactions..."
    for trans in sim.generate_transactions(end_time=365.0):
        trans_writer.append(trans)

    print

    trans_writer.close()
