package spinal.tester.scalatest

import org.scalatest.FunSuite
import spinal.tester
import spinal.core._
import spinal.core.sim._
import spinal.sim._
import spinal.lib._
import spinal.lib.bus.misc._
import spinal.lib.bus.wishbone._
import spinal.lib.wishbone.sim._
import spinal.lib.sim._
import scala.util.Random

class WishboneInterconComponent(config : WishboneConfig,decodings : Seq[SizeMapping]) extends Component{
  val io = new Bundle{
    val busMasters = slave(Wishbone(config))
    val busSlaves = Vec(master(Wishbone(config)),decodings.size)
  }
  val intercon = WishboneInterconFactory()
  intercon.addMasters(busMasters)
  intercon.addSlaves(busSlaves)
}

class SpinalSimWishboneDecoderTester extends FunSuite{
  def testIntercon(config : WishboneConfig,decodings : Seq[SizeMapping],description : String = ""): Unit = {
    val fixture = SimConfig.allOptimisation.withWave.compile(rtl = new WishboneDecoderComponent(config,decodings))
    fixture.doSim(description){ dut =>
      def send_transaction(master,data, address): Unit@suspendable = fork{
        
      }
      // dut.clockDomain.forkStimulus(period=10)
      // dut.io.busIN.CYC #= false
      // dut.io.busIN.STB #= false
      // dut.io.busIN.WE #= false
      // dut.io.busIN.ADR #= 0
      // dut.io.busIN.DAT_MOSI #= 0
      // dut.io.busOUT.suspendable.foreach{ bus =>
      //   if(bus.config.isPipelined) bus.STALL #= false
      //   bus.ACK #= false
      //   bus.DAT_MOSI #= 0
      // }
      // dut.clockDomain.waitSampling(10)
      // SimTimeout(1000*10000)
      // val sco = ScoreboardInOrder[WishboneTransaction]()
      // val dri = new WishboneDriver(dut.io.busIN, dut.clockDomain)
      // val maxAddr = 2099

      // val seq = WishboneSequencer{
      //   WishboneTransaction().randomizeAddress(maxAddr).randomizeData(200)
      // }

      // val monIN = WishboneMonitor(dut.io.busIN, dut.clockDomain){ bus =>
      //   sco.pushRef(WishboneTransaction.sampleAsMaster(bus))
      // }

      // dut.io.busOUT.suspendable.foreach{busOut =>
      //   WishboneMonitor(busOut, dut.clockDomain){ bus =>
      //   sco.pushDut(WishboneTransaction.sampleAsMaster(bus))
      //   }
      // }

      // dut.io.busOUT.suspendable.foreach{busOut =>
      //   val driver = new WishboneDriver(busOut, dut.clockDomain)
      //   driver.slaveSink()
      // }

      // Suspendable.repeat(100){
      //   seq.generateTransactions(1000)
      //   val ddd = fork{
      //     while(!seq.isEmpty){
      //       val tran = seq.nextTransaction
      //       dri.drive(tran ,true)
      //       dut.clockDomain.waitSampling(1)
      //     }
      //   }
      //   ddd.join()
      //   dut.clockDomain.waitSampling(10)
      // }
    }
  }

  test("classicWishboneDecoder"){
    val size = 100
    val config = WishboneConfig(32,8)
    val decodings = for(i <- 1 to 20) yield SizeMapping(i*size,size-1)
    testDecoder(config,decodings,"classicWishboneDecoder")
  }

  test("pipelinedWishboneDecoder"){
    val size = 100
    val config = WishboneConfig(32,8).pipelined
    val decodings = for(i <- 1 to 20) yield SizeMapping(i*size,size-1)
    testDecoder(config,decodings,"pipelinedWishboneDecoder")
  }
}