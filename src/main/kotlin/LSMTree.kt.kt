import storage.SSTable
import java.io.File
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicLong

class LSMTree {
  private val dir: String = "lsm"
  private val memTableSizeLimit: Int = 1024 * 1024

  private val memTable = ConcurrentSkipListMap<String, String>()
  private val immutableMemTables = mutableListOf<ConcurrentSkipListMap<String, String>>()
  private val ssTables = mutableListOf<SSTable>()
  private val sequence = AtomicLong(0)

  init {
    File(dir).mkdirs()
    // TODO: load existing SSTables from disk
  }

  fun get(key: String): String? {
    return null
  }

  fun put(key: String, value: String) {}

  fun delete(key: String) {}
}
