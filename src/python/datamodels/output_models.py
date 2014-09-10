import hashlib

class Store(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.location = None

    def __repr__(self):
        return "%s,%s,%s" % (self.name, self.location.zipcode, self.location.coords)

class Customer(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.location = None

    def __repr__(self):
        return "(%s, %s, %s)" % \
            (self.id, self.name, self.location)

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
