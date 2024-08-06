import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import services.transfertServices.Send


class Main : CliktCommand(){
    val receive : Boolean by option(
        "--receive", "-r", help =  "specify that SEND will receive a file but not will send"
    )
        .flag( default = false)

    val file by option("--file","-f", help = "filepath to transfer")
    val address by option("--address", "-a", help = "address where to send the file")
    val port by option("--pot","-p", help = "--adress port where to send the file").default("2610")

    override fun run() {
        // Default behavior = Sending file
        if (receive) {
            echo("Receiving file... ")
        } else {
            if (file != null && address!= null ) {
                echo("Sending file (default)...  ")
                val sendService = port.toInt().let { Send(file!!, address!!, it) }
                sendService?.process()

            } else{
                echo("--file and --address must be provided")
            }

        }

    }

}


fun main(args: Array<String>) {
    Main().main(args)
}