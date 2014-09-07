import json

from datamodels.input_models import ProductCategory

from zipcodes import load_zipcode_data

def load_names(namedb_fl):
    name_fl = open(namedb_fl)
    first_names = []
    last_names = []
    for ln in name_fl:
        cols = ln.strip().split(",")
        name = cols[0]
        weight = float(cols[5])
        if cols[4] == "1":
            first_names.append((name, weight))
        if cols[3] == "1":
            last_names.append((name, weight))
    name_fl.close()
    return first_names, last_names

def load_products(products_fl):
    category_fl = open(products_fl)
    product_categories = json.load(category_fl)
    category_fl.close()

    product_category_objects = dict()
    for category in product_categories:
        product_category_objects[category["category"]] = ProductCategory(**category)

    return product_category_objects
