package storage.index

import java.io.*

/**
 * Sparse index for [SSTable](storage.SSTable) lookups.
 */
class SparseIndex {
  private val entries = mutableListOf<IndexEntry>()

  data class IndexEntry(val key: String, val offset: Long)

  fun addEntry(key: String, offset: Long) {
    entries.add(IndexEntry(key, offset))
  }

  fun save(filePath: String) {
    DataOutputStream(FileOutputStream(filePath)).use { out ->
      out.writeInt(entries.size)
      for (entry in entries) {
        out.writeUTF(entry.key)
        out.writeLong(entry.offset)
      }
    }
  }

  fun findNearest(key: String): Long {
    if (entries.isEmpty()) return 0L

    var left = 0
    var right = entries.size - 1
    var result = 0L

    while (left <= right) {
      val mid = (right + left) / 2
      val midKey = entries[mid].key

      when {
        midKey <= key -> {
          result = entries[mid].offset
          left = mid + 1
        }

        else -> right = mid - 1
      }
    }

    return result
  }

  companion object {
    fun load(filePath: String): SparseIndex {
      val index = SparseIndex()

      try {
        DataInputStream(FileInputStream(filePath)).use { input ->
          val count = input.readInt()
          repeat(count) {
            val key = input.readUTF()
            val offset = input.readLong()
            index.addEntry(key, offset)
          }
        }
      } catch (e: IOException) {
        println("Failed to load SparseIndex file $filePath: ${e.message}")
      }

      return index
    }
  }
}