package com.pwootage.metroidprime.formats.iso

case class FileEntry(name: String, offset: Int, length: Int)
case class FileDirectory(name: String, var fileChildren: Seq[FileEntry], var directoryChildren: Seq[FileDirectory]) {
  def recursivelyCalculateEntryCount: Int = {
    fileChildren.length +
      directoryChildren.length +
      directoryChildren.map(_.recursivelyCalculateEntryCount).sum
  }
}