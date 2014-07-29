# parameters for simulation

TRANSACTION_TRIGGER_TIME_AVERAGE = 5.0 # days
TRANSACTION_TRIGGER_TIME_VARIANCE = 2.0
TRANSACTION_TRIGGER_TIME_MAX = 10.0
TRANSACTION_TRIGGER_TIME_MIN = 1.0

PURCHASE_TRIGGER_TIME_AVERAGE = 10.0
PURCHASE_TRIGGER_TIME_VARIANCE = 4.0
PURCHASE_TRIGGER_TIME_MAX = 20.0
PURCHASE_TRIGGER_TIME_MIN = 1.0

AVERAGE_CUSTOMER_STORE_DISTANCE = 5.0 # miles

MAX_PETS = 10
MIN_PETS = 1

STORE_INCOME_SCALING_FACTOR = 100.0

PRODUCT_CATEGORY_PARAMS = [
	{
		"category" : "dry dog food",
		"species" : ["dog"],
		"trigger_transaction" : True,
		"fields" : ["_brand", "_meat", "_grain", "quantity",
			 "_limited_ingredient", "_age", "_weight_loss",
			 "_organic"],
		"daily_usage_rate" : 2.0, # times/day
		"base_amount_used_average" : 0.25, # quantity/use
		"base_amount_used_variance" : 0.1, # quantity/use
		"transaction_trigger_rate" : 2.0, # days/transaction
		"transaction_purchase_rate" : 7.0 # days/transaction
	},
	{
		"category" : "dry cat food",
		"species" : ["cat"],
		"trigger_transaction" : True,
		"fields" : ["_brand", "_meat", "_grain", "quantity",
			 "_limited_ingredient", "_age", "_hairball_management",
			 "_organic"],
		"daily_usage_rate" : 2.0,
		"base_amount_used_average" : 0.1,
		"base_amount_used_variance" : 0.05,
		"transaction_trigger_rate" : 2.0,
		"transaction_purchase_rate" : 7.0
	},
	{
		"category" : "kitty litter",
		"species" : ["cat"],
		"trigger_transaction" : True,
		"fields" : ["_brand", "_scent", "_odor_control", "quantity"],
		"daily_usage_rate" : 1.0,
		"base_amount_used_average" : 0.1,
		"base_amount_used_variance" : 0.05,
		"transaction_trigger_rate" : 2.0,
		"transaction_purchase_rate" : 7.0
	},
	{
		"category" : "poop bags",
		"species" : ["dog"],
		"trigger_transaction" : True,
		"fields" : ["_brand", "_color", "quantity"],
		"daily_usage_rate" : 2.0,
		"base_amount_used_average" : 1.0,
		"base_amount_used_variance" : 0.5,
		"transaction_trigger_rate" : 2.0,
		"transaction_purchase_rate" : 7.0
	}
]
