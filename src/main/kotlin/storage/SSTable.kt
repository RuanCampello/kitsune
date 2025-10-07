package storage

/**
 * Sorted String Table: immutable on-disk storage
 */
class SSTable(
  private val filePath: String,
  private val indexPath: String,
  private val bloomFilterPath: String,
) {
  
}