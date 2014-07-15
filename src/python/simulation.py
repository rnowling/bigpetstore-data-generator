import products

from customers import CustomerGenerator
from customer_simulation import CustomerState

from transactions import TransactionSimulator


class Simulator(object):
    def __init__(self):
        self.item_categories = products.load_products_json()

    def generate_customers(self, num=None):
        return CustomerGenerator().generate(num)

    def generate_transactions(self, customer=None, end_time=None):
        state = CustomerState(item_categories=self.item_categories,
                customer=customer)
        trans_sim = TransactionSimulator(customer_state=state)
        for trans in trans_sim.simulate(end_time):
            yield trans

    def simulate(self, num_customers=None, end_time=None):
        customers = self.generate_customers(num=num_customers)

        for customer in customers:
            for trans in self.generate_transactions(customer=customer, end_time=end_time):
                yield trans

class TransactionWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, trans):
        for item in trans.purchased_items:
            values = [
                trans.customer.name,
                trans.trans_time,
                item
                ]
            string = ",".join(map(str, values)) + "\n"
            self.fl.write(string)

    def close(self):
        self.fl.close()

if __name__ == "__main__":
    sim = Simulator()
    trans_writer = TransactionWriter(filename="transactions.txt")
    for trans in sim.simulate(num_customers=100, end_time=365.0*4.0):
        trans_writer.append(trans)

    trans_writer.close()
