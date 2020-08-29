import sys
import random
import math
import time
import itertools
import csv
from tqdm import tqdm


def readFile(fileName):
	f = open(fileName, "r")
	driverNum = int(f.readline())
	drivers = {}  # drivers stored in dict {player num: player name}
	for i in range(driverNum):
		driver = f.readline().split(',')
		drivers[int(driver[0])] = driver[1].rstrip()

	# Some perhaps unnecessary variables
	setupData = f.readline().split(',')
	racesNum = int(setupData[0])
	dataPref = int(setupData[1])
	pairWiseNum = int(setupData[2])

	# add weights and players to list
	weights = []

	# while there are more weights to go run
	weightsToGo = True
	while weightsToGo:
		weightData = f.readline().split(',')
		if len(weightData) == 3:
			entery = [int(weightData[0]), [int(weightData[1]), int(weightData[2])]]  # <weight, edge>
			weights.append(entery)
		# end of weights
		else:
			weightsToGo = False

	return drivers, weights, racesNum, dataPref, pairWiseNum


# returns a matrix of payers and weights
def makeMatrix(weights, numDrivers):
	# matrix has size numDrivers x numDrivers
	matrix = []
	for i in range(numDrivers):
		matrix.append([0] * numDrivers)

	for i in weights:
		# matrix[y][x] = weight
		# y beats x by weight
		matrix[i[1][0] - 1][i[1][1] - 1] = i[0]

	return matrix


# Returns single score for a given driver
def kemenyScore(driverIndex, ranking, weightMatrix):
	score = 0
	for i in range(len(ranking) - driverIndex):
		score += weightMatrix[ranking[i + driverIndex] - 1][ranking[driverIndex] - 1]
	return score


# Returns the complete keneny scores for the ranking
def kemenyScores(ranking, weightMatrix):
	scores = []
	for i in range(len(ranking)):
		scores.append(kemenyScore(i, ranking, weightMatrix))
	return scores


# Update all kemeny scores from the index a to the index b
# Returns all scores, new scores, replaced scores
def updateScores(a, b, ranking, scores, weightMatrix):
	# if a or b out of range then wrap to start / end of list
	if a < 0:
		a = len(ranking) - 1
	elif a > len(ranking) - 1:
		a = 0
	if b < 0:
		b = len(ranking) - 1
	elif b > len(ranking) - 1:
		b = 0

	newScores = []
	if a < b:
		replacedScores = scores[a:b + 1]  # scores from section being replaces
		for i in range(a, b + 1):
			newScores.append(kemenyScore(i, ranking, weightMatrix))
		return scores[0:a] + newScores + scores[b + 1::], newScores, replacedScores
	else:
		replacedScores = scores[b:a + 1]  # scores from section being replcaed
		for i in range(b, a + 1):
			newScores.append(kemenyScore(i, ranking, weightMatrix))
		return scores[0:b] + newScores + scores[a + 1::], newScores, replacedScores


# Simulated annealing algorithem
def simAnnealing(TI, TL, a, numNonImprove, x, cost, weightMatrix):
	currentRanking = x[:]  # Set inital solution
	currentCost = cost[:]  # cost relating to inital solution
	bestSolution = x[:]  # the current best solution
	bestCost = cost[:]  # cost relating to the best solution
	T = TI  # set inital temp
	uphillMoves = 0  # counter for number of uphill moves since bestSolution update
	movesSinceLastBest = 0  # counter for number of moves since last best solution
	while movesSinceLastBest < numNonImprove:
		for i in range(TL):
			# Generate new neighbourhood solution
			newRanking = currentRanking[:]
			newCost = currentCost[:]
			costSum = sum(currentCost)
			toChange = random.randint(0, len(x) - 2)  # rendomly pick index driver to move
			driverA = newRanking[toChange]
			driverB = newRanking[toChange + 1]
			newRanking[toChange + 1] = driverA
			newRanking[toChange] = driverB  # move driverB to driverA old location
			# compute new cost
			newCost, newCostPart, oldCostPart = updateScores(toChange + 1, toChange, newRanking, newCost, weightsMatrix)
			newCostSum = sum(newCost)
			# If new cost is higher than last then run next step to find out if it will be kept
			if newCostSum > costSum:
				q = random.random()
				# e to the power of (-∆C/T)
				# if q is smaller then update current ranking and costs
				if q < (math.e ** (-(newCostSum - costSum) / T)):
					currentRanking = newRanking
					currentCost = newCost
					uphillMoves += 1  # add one to uphill move counter
					movesSinceLastBest += 1
			# If new cost is lower or equal than old cost then keep the new solution
			else:
				currentRanking = newRanking
				currentCost = newCost
				movesSinceLastBest += 1
				# If the solution is the best seen so far then updated
				if sum(bestCost) > sum(currentCost):
					bestSolution = currentRanking
					bestCost = currentCost
					movesSinceLastBest = 0
		T = a * T  # update the tempature

	return bestSolution, bestCost, uphillMoves


drivers, weights, racesNum, dataPref, pairWiseNum = readFile(sys.argv[1])  # File to use taken as argument

# Set up parameters
weightsMatrix = makeMatrix(weights, len(drivers))
ranking = list(range(1, len(drivers) + 1))  # initial solution

T = 1  # Inital temp (free to experiment)
TL = 100  # Temp length (free to experiment - increase as temp goes down)
a = 0.9995  # For cooling ration f(T), T = a*T (free to experiment with a)
numNonImprove = 20000  # Stopping critera (free to experiment)
# cost function = c(Kemeny score, tournament loaded)
kemenyScores = kemenyScores(ranking, weightsMatrix)


"""

Tvars = [1, 2, 5, 10, 15, 20, 30, 40, 50, 100]
TLvars = [1, 2, 5, 10, 20, 30, 40, 50, 100, 150]
aVars = [0.9995, 0.99, 0.95, 0.90, 0.85, 0.80]  # cannot do lower than 0.8 as T becomes 0 (Python underflow)
numNonImproveVars = [100, 500, 1000, 1500, 2000, 5000, 10000, 15000, 20000]  # cannot do 25000 as T becomes 0 (Python underflow)

variables = [Tvars, TLvars, aVars, numNonImproveVars]
allCombos = list(itertools.product(*variables))
allSolutions = []

for i in tqdm(range(len(allCombos)), ascii=True, unit='combination'):
	combo = allCombos[i]
	for j in range(10):
		startTime = time.perf_counter_ns()
		solution, solutionCost, uphillMoves = simAnnealing(combo[0], combo[1], combo[2], combo[3], ranking, kemenyScores, weightsMatrix)
		elapsedTime = time.perf_counter_ns() - startTime
		allSolutions.append([sum(solutionCost), uphillMoves, elapsedTime/1000000, [combo[0], combo[1], combo[2], combo[3]]])

with open('output.csv','w') as result_file:
	wr = csv.writer(result_file)
	wr.writerows(allSolutions)

"""


startTime = time.perf_counter_ns()
solution, solutionCost, uphillMoves = simAnnealing(T, TL, a, numNonImprove, ranking, kemenyScores, weightsMatrix)
elapsedTime = time.perf_counter_ns() - startTime

print("Rank\tName")
print("----\t----")
for i in range(len(solution)):
	print(str(i + 1) + "\t" + drivers.get(solution[i]))

print("Kemeny score:", sum(solutionCost))
print("Runtime:", elapsedTime/1000000, "(milliseconds)")
print("Uphill moves:", uphillMoves)
