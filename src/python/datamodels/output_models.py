class Store(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.zipcode = None
        self.coords = None

    def __repr__(self):
        return "%s,%s,%s" % (self.name, self.zipcode, self.coords)
