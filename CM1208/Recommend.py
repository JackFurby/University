#Student number: 1619450

import math
import numpy as np
import itertools
import bisect

#defines variables
historyObject = open("history.txt", "r")
queriesObject = open("queries.txt", "r")
historyList = []
purchaseHistoryTable = []
customerTotal = 0
averageAngle = 0
angleTotal = 0
Angle_List={}

#puts elements in history.txt into a list
for i in historyObject:
    i = i.replace("\n","")
    i = i.split(" ")
    historyList.append(i)

#defines a few values from first line of history.txt
Number_of_Customers = historyList[0][0]
Number_of_Items = historyList[0][1]
Number_of_Transactions = historyList[0][2]

#makes purchase history for each item
for i in range(0, int(Number_of_Items)):
    purchaseHistoryTableSingle = [0 for x in range(int(Number_of_Customers))]
    purchaseHistoryTable.append(purchaseHistoryTableSingle)

#adds customer item history to each item
for i in historyList[1:]:
    purchaseHistoryTable[int(i[1])-1][int(i[0])-1] = 1

#works out Positive entries (all 1's in purchaseHistoryTable)
for i in purchaseHistoryTable:
    customerTotal += sum(i)
print("Positive entries: " + str(customerTotal))

 #calculates angle
def calcAngle(x, y):
    normX = np.linalg.norm(x)
    normY = np.linalg.norm(y)
    normMulti = normX * normY
    if normMulti == 0:
        return 90.0
    cosTheta = np.dot(x, y) / (normMulti)
    theta = math.degrees(math.acos(cosTheta))
    return theta

#makes list of all possible combinations of items
itemCombo = list(itertools.combinations([x+1 for x in range(int(Number_of_Items))], 2))

#calculate average angle for items
for i in itemCombo:
    Angle_List[i] = (calcAngle(purchaseHistoryTable[int(i[0])-1], purchaseHistoryTable[int(i[1])-1]))
    Angle_List[tuple(reversed(i))] = Angle_List[i]

averageAngle = sum([Angle_List[angle] for angle in Angle_List]) / (len(itemCombo)*2)
print("Average angle: " + str(round(averageAngle,2)))

#works out recommendations for different items
for i in queriesObject: #for line in queriesObject
    Recommend_List = []
    print("Shopping cart: " + i.rstrip()) #prints items to query without return
    basket = [int(x) for x in i.split()]
    for j in basket: #iterate through items to query
        Item_ID = " no match"
        Match_ID = ""
        Min_Angle = 90.0
        counter = 0 #counter used to work out if all matches have been tried
        for k in range(int(Number_of_Items)): #iterate through items
            counter += 1
            if (k + 1 != j): #for item if it is not = item to compare against
                if int(k) + 1 not in basket: #check to see if k + 1 is in items to query
                    temp_val = Angle_List[(int(j)), (int(k) + 1)] #gets angle
                    if temp_val < Min_Angle:
                        Min_Angle = round(temp_val,2)
                        Item_ID = str(j)
                        Match_ID = str(int(k)+1)
                        bestMatch = int(k)+1
                if counter == int(Number_of_Items) and Min_Angle != 90.0: #if all comparisons for the item are done
                    bisect.insort_left(Recommend_List,(Min_Angle, str(bestMatch))) #creates a sorted list by angle size
                    print("Item: "+ Item_ID + "; match: " + Match_ID + "; angle: " + str('%.2f' % Min_Angle))
                elif counter == int(Number_of_Items) -1 and Min_Angle == 90.0: #no items match current item
                    print("Item: " + str(j) + Item_ID)
    seen = set()
    seen_add = seen.add
    print("Recommend: " + " ".join([recomend[1] for recomend in Recommend_List if not (recomend[1] in seen or seen_add(recomend[1]))]))

    #order?, H. (2009). How do you remove duplicates from a list in whilst preserving order?. [online] Stackoverflow.com. Available at: http://stackoverflow.com/questions/480214/how-do-you-remove-duplicates-from-a-list-in-whilst-preserving-order [Accessed 29 Mar. 2017].
