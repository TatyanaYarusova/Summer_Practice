import java.lang.Exception
import java.util.*
import kotlin.math.abs

class Graph() {
	private var startNode: Char? = null
	private var goalNode: Char? = null
	private val nodes: MutableMap<Char, MutableList<Pair<Char, Double>>> = mutableMapOf()
	private var path: String = ""

	fun readInfo() {
		val startEndGoalNode = readln().split(" ")
		startNode = startEndGoalNode[0].single()
		goalNode = startEndGoalNode[1].single()

		while (true) {
			try {
				val stringInfo: String = readln()

				val (fromNode, inNode, weight) = stringInfo.split(" ")

				if (nodes.containsKey(fromNode.single())) {
					nodes[fromNode.single()]?.add(Pair(inNode.single(), weight.toDouble()))
				} else {
					nodes[fromNode.single()] = mutableListOf(Pair(inNode.single(), weight.toDouble()))
				}
			} catch (e: Exception) {
				break
			}
		}
	}

	private fun heuristics(currentNode: Char): Double {
		return abs(currentNode.code - goalNode!!.code).toDouble()
	}

	private fun getNeighbors(currentNode: Char?): MutableList<Pair<Char, Double>>? {
		return nodes[currentNode]
	}

	private fun buildPath(cameFrom: MutableMap<Char?, Char?>) {
		var currentNode = goalNode
		while (currentNode != null) {
			path += currentNode
			currentNode = cameFrom[currentNode]
		}
		path = path.reversed()
		println(path)
	}

	fun aStar() {
		val priorityQueueNodes = PriorityQueue(compareBy<Pair<Double, Char>> { it.first }.thenByDescending { it.second.code })
		priorityQueueNodes.add(Pair(0.0, startNode!!))

		val cameFrom: MutableMap<Char?, Char?> = mutableMapOf()
		cameFrom[startNode] = null

		val costSoFar: MutableMap<Char, Double> = mutableMapOf()
		costSoFar[startNode!!] = 0.0

		while (!priorityQueueNodes.isEmpty()) {
			val currentNode: Char = priorityQueueNodes.remove().second

			if (currentNode == goalNode) break

			if (nodes.containsKey(currentNode)) {
				val neighbors = getNeighbors(currentNode)
				neighbors?.forEach {
					val newCost = costSoFar[currentNode]!! + it.second
					if ((!costSoFar.containsKey(it.first)) || (newCost < costSoFar[it.first]!!)) {
						costSoFar[it.first] = newCost
						val priority: Double = newCost + heuristics(it.first)
						priorityQueueNodes.add(Pair(priority, it.first))
						cameFrom[it.first] = currentNode
					}
				}
			}
		}
		buildPath(cameFrom)
	}
}

fun solve() {
	val graph = Graph()
	graph.readInfo()
	graph.aStar()
}

fun main() {
	solve()
}
