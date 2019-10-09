import sys

num_txn      = int(sys.argv[1])

f2 = open("tee_data", "w")

# Rest of the transactions
for i in range(0, num_txn):
    from_name = 'A' + str(i)
    to_name = 'A' + str(i+1)
    f2.write(''+from_name+','+to_name+',1'+"\n")

f2.close()