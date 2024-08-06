package services.transfertServices

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

class Send(
    private val path: String,
    private val address: String,
    private val port: Int
) {
    private lateinit var file: File
    private lateinit var socket: Socket
    private lateinit var inputStream: FileInputStream
    private lateinit var output: OutputStream

    fun process() {
        configure()
        try {
            val buffer = ByteArray(8192) // Increased buffer size for efficiency
            output = socket.getOutputStream()
            println("Transferring ${file.name} to $address:$port ...")
            var bytesRead: Int

            inputStream.use { input ->
                output.use { out ->
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        out.write(buffer, 0, bytesRead)
                    }
                    out.flush() // Ensure all data is sent
                }
            }
            println("Transfer complete : ${file.name}")
        } catch (e: IOException) {
            println("Error during file transfer: ${e.message}")
            e.printStackTrace()
        } finally {
            try {
                socket.close()
            } catch (e: IOException) {
                println("Error closing socket: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun configure() {
        try {
            file = File(path)
            if (!file.exists() || !file.isFile) {
                throw IOException("File does not exist or is not a file: $path")
            }
            socket = Socket(address, port)
            inputStream = FileInputStream(file)
        } catch (e: IOException) {
            println("Error configuring file transfer: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Reads and prints the content of the file line by line.
     */
    fun readAndPrintLine() {
        try {
            FileInputStream(file).use { input ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    val str = String(buffer, 0, bytesRead)
                    println(str)
                }
            }
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            e.printStackTrace()
        }
    }
}
