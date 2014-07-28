DOG_FOOD_FIELDS = {
    "base_price" : 10.79,
    "brand" : [("Wellfed", 1.0, 0.0), ("Happy Pup", 1.0, 0.0), ("Dog Days", 1.0, 0.0), ("Chef Corgie", 1.0, 0.0), ("Nature's Cornucopia", 1.0, 0.0)],
    "meat" : [("Chicken", 1.0, 0.0), ("Pork", 1.0, 0.0), ("Beef", 1.0, 0.0), ("Lamb", 1.0, 0.0), ("Bison", 1.0, 0.0), ("Venison", 1.0, 0.0), ("Rabbit", 1.0, 0.0), ("Salmon", 1.0, 0.0), ("Vegetarian", 1.0, 0.0)],
    "grain" : [("Potato", 1.0, 0.0), ("Sweet Potato", 1.0, 0.0), ("Rice", 1.0, 0.0), ("Brown Rice", 1.0, 0.0), ("Soy", 1.0, 0.0), ("Barley", 1.0, 0.0)],
    "quantity" : [(4.5, 1.0, 0.0), (15.0, 2.50, 0.0), (30.0, 3.69, 0.0)],
    "limited_ingredient" : [(True, 1.0, 0.0), (False, 1.0, 0.0)],
    "age" : [("Puppy", 1.0, 0.0), ("Senior", 1.0, 0.0), ("Adult", 1.0, 0.0)],
    "weight_loss" : [(True, 1.0, 0.0), (False, 1.0, 0.0)],
    "organic" : [(True, 1.0, 0.0), (False, 1.0, 0.0)]
}

def combination_generator(field_name, choices, previous=None):
    if previous == None:
        for choice in choices:
            d = dict()
            d[field_name] = choice
            yield d
    else:
        for previous_state in previous:
            for choice in choices:
                previous_state = previous_state.copy()
                previous_state[field_name] = choice
                yield previous_state

# internal vs external fields
# limiting values based on other values e.g, limited ingredient for only certain brands

def generate_dog_food(food_fields):
    field_names = food_fields.keys()
    generator = None
    for name in field_names:
        if name == "base_price":
            continue
        generator = combination_generator(name, food_fields[name], previous=generator)
    items = [item for item in generator]

    base_price = food_fields["base_price"]
    processed_items = []
    for item in items:
        processed_item = dict()
        multipliers = 1.0
        additions = 0.0
        for field_name, (value, multiplier, addition) in item.iteritems():
            multipliers *= multiplier
            additions += addition
            processed_item[field_name] = value
        price = multipliers * (base_price + additions)
        processed_item["price"] = price
        processed_item["per_unit_cost"] = processed_item["price"] / processed_item["quantity"]
        processed_item["description"] = "%s %s & %s" % (processed_item["brand"],
                                                       processed_item["meat"],
                                                       processed_item["grain"])
        print processed_item


if __name__ == "__main__":
    generate_dog_food(DOG_FOOD_FIELDS)
    
    
    
        
