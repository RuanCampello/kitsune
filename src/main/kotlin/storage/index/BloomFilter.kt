package storage.index;

import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max

class BloomFilter(
  private val expectedElements: Int,
  private val falsePositiveRate: Double,
) {
  private val array: BitSet
  private val hashFunctions: Int
  private val arraySize: Int

  init {
    array = BitSet(expectedElements)
    arraySize = calculateArraySize(expectedElements, falsePositiveRate)
    hashFunctions = calculateHashFunctions(arraySize, expectedElements)
  }

  fun add(key: String) {
    for (idx in 0 until hashFunctions) {
      val hash = hash(key, idx) % arraySize
      array.set(abs(hash))
    }
  }

  fun contains(key: String): Boolean {
    for (idx in 0 until hashFunctions) {
      val hash = hash(key, idx) % arraySize
      if (!array.get(abs(hash))) {
        return false
      }
    }

    return true
  }

  fun save(filePath: String) {
    ObjectOutputStream(FileOutputStream(filePath)).use { out ->
      out.writeObject(array)
      out.writeInt(hashFunctions)
      out.writeInt(arraySize)
    }
  }

  private fun hash(key: String, seed: Int): Int {
    var hash = seed
    for (char in key) {
      hash = hash * 31 + char.code
    }

    return hash
  }

  companion object {
    private fun calculateArraySize(expectedElements: Int, falsePositiveRate: Double): Int {
      return (-expectedElements * ln(falsePositiveRate) / (ln(2.0) * ln(2.0))).toInt()
    }

    private fun calculateHashFunctions(arraySize: Int, expectedElements: Int): Int {
      return max(1, (arraySize.toDouble() / expectedElements * ln(2.0)).toInt())
    }
  }
}
