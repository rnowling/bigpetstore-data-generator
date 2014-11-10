def read_income_data(flname):
    fl = open(flname)
    #skip headers
    next(fl)
    next(fl)
    zipcode_incomes = dict()
    for ln in fl:
        cols = ln.strip().split(",")
        # zipcodes in column 3 in the format "ZCTA5 XXXXX"
        zipcode = cols[2].split()[1]
        try:
            median_household_income = int(cols[5])
            zipcode_incomes[zipcode] = median_household_income
        except:
            # some records do not contain incomes
            pass
    fl.close()
    return zipcode_incomes

def read_population_data(flname):
	fl = open(flname)
	pop_data = dict()
	# skip headers
	next(fl)
	for ln in fl:
		if ln.strip() == "":
			continue
		zipcode, pop = ln.split(",")
		pop = int(pop)
        # remove duplicates.  keep largest pop values
		if zipcode in pop_data:
			pop_data[zipcode] = max(pop_data[zipcode], pop)
		else:
			pop_data[zipcode] = pop
	fl.close()
        return pop_data

def read_zipcode_coords(flname):
    fl = open(flname)
    # skip header
    next(fl)
    zipcode_coords = dict()
    for ln in fl:
        cols = ln.split(", ")
        zipcode = cols[0][1:-1] # remove double-quote marks
        lat = float(cols[2][1:-1])
        long = float(cols[3][1:-1])
        zipcode_coords[zipcode] = (lat, long)
    fl.close()
    return zipcode_coords

class ZipcodeData(object):
    def __init__(self, zipcode=None, median_household_income=None,
                 population=None, coords=None):
        self.zipcode = zipcode
        self.median_household_income = median_household_income
        self.population = population
        self.coords = coords

def load_zipcode_data():
    zipcode_incomes = read_income_data("../../resources/ACS_12_5YR_S1903/ACS_12_5YR_S1903_with_ann.csv")
    zipcode_pop = read_population_data("../../resources/population_data.csv")
    zipcode_coords = read_zipcode_coords("../../resources/zips.csv")

    all_zipcodes = set(zipcode_incomes.keys()).intersection(set(zipcode_pop.keys())).intersection(set(zipcode_coords))

    zipcode_objects = dict()
    for z in all_zipcodes:
        obj = ZipcodeData(zipcode=z,
                          median_household_income=zipcode_incomes[z],
                          population=zipcode_pop[z],
                          coords=zipcode_coords[z])
        zipcode_objects[z] = obj

    return zipcode_objects


if __name__ == "__main__":
    zipcode_objects = load_zipcode_data()

    print len(zipcode_objects)
