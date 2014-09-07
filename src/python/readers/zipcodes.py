from datamodels.input_models import ZipcodeRecord

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
            median_household_income = float(cols[5])
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


def load_zipcode_data(income_fl=None, population_fl=None, coordinate_fl=None):
    zipcode_incomes = read_income_data(income_fl)
    zipcode_pop = read_population_data(population_fl)
    zipcode_coords = read_zipcode_coords(coordinate_fl)

    all_zipcodes = set(zipcode_incomes.keys()).intersection(set(zipcode_pop.keys())).intersection(set(zipcode_coords))

    zipcode_objects = dict()
    for z in all_zipcodes:
        obj = ZipcodeRecord(zipcode=z,
                            median_household_income=zipcode_incomes[z],
                            population=zipcode_pop[z],
                            coords=zipcode_coords[z])
        zipcode_objects[z] = obj

    return zipcode_objects


