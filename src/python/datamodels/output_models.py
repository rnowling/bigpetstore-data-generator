import hashlib

class Store(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.zipcode = None
        self.coords = None

    def __repr__(self):
        return "%s,%s,%s" % (self.name, self.zipcode, self.coords)

class Customer(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.location = None
        self.average_transaction_trigger_time = None
        self.average_purchase_trigger_time = None
        self.pets = {
                        "dog" : 0,
                        "cat" : 0
                    }

    def __repr__(self):
        return "(%s, %s, %s dogs, %s cats, %s)" % \
            (self.id, self.name, self.pets["dog"], 
             self.pets["cat"], self.location)

class Transaction(object):
    def __init__(self, customer=None, trans_time=None, purchased_items=None, store=None,
                 trans_count=None):
        self.store = store
        self.customer = customer
        self.trans_time = trans_time
        self.purchased_items = purchased_items
        self.trans_count = trans_count

    def transaction_id(self):
        return hashlib.md5(repr(self)).hexdigest()

    def __repr__(self):
        return "(%s, %s, %s, %s)" % (self.store.id,
                                     self.customer.id,
                                     self.trans_time,
                                     self.trans_count)
