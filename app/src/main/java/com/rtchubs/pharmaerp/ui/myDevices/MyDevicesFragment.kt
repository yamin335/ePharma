package com.rtchubs.pharmaerp.ui.myDevices

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.models.MessageModel
import com.rtchubs.pharmaerp.databinding.MyDevicesFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.io.UnsupportedEncodingException

private const val USERNAME = "ParkRFID"
private const val PASSWORD = "4PRFID0nly"
private const val HOST ="54.255.229.67"
private const val PORT = 1883
//var TOPIC = "rfid/message"
class MyDevicesFragment : BaseFragment<MyDevicesFragmentBinding, MyDevicesViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_my_devices
    override val viewModel: MyDevicesViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var adapter: MyDeviceListAdapter
    var client: MqttAndroidClient? = null
    private lateinit var token: IMqttToken

    var topic: String = "rfid/message"

    private val messages = arrayListOf(
//        MessageModel("000000001", R.drawable.img_0), MessageModel("48229571", R.drawable.img_1),
        MessageModel("49093351", R.drawable.img_2),
        MessageModel("48879751", R.drawable.img_4),
        MessageModel("48821271", R.drawable.img_6)
//        MessageModel("48799541", R.drawable.img_8), MessageModel("49139421", R.drawable.img_9),
//        MessageModel("48996531", R.drawable.img_10), MessageModel("48597591", R.drawable.img_11),
//        MessageModel("48451191", R.drawable.img_12), MessageModel("47687151", R.drawable.img_13),
//        MessageModel("47597751", R.drawable.img_14), MessageModel("47480761", R.drawable.img_15),
//        MessageModel("47810491", R.drawable.img_16), MessageModel("48451071", R.drawable.img_17)
    )

    override fun onStart() {
        super.onStart()
        client?.registerResources(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()

        client?.disconnect()?.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {

            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {

            }
        }
        client?.unregisterResources()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val clientId = MqttClient.generateClientId()
        val uri = "tcp://$HOST:$PORT"

        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.userName = USERNAME
        mqttConnectOptions.password = PASSWORD.toCharArray()

        client = MqttAndroidClient(requireActivity(), uri, clientId)
        client?.connect(mqttConnectOptions)?.let {
            token = it
        }

        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {

            }

            @SuppressLint("LongLogTag")
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                exception?.message?.let { Log.e("MqttClient ERROR: ", it)}
            }
        }

        client?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.e("Mqtt Status: ", "CONNECTED!!")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.toString()?.let {
                    //adapter.setMessage(message = it)
                    //subscribe(it)
                    Log.e("Mqtt Message: ", it)
                }

                Log.e("Mqtt Status: ", "MESSAGE ARRIVED!!")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e("Mqtt Status: ", "CONNECTION LOST!!")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.e("Mqtt Status: ", "MESSAGE DELIVERED!!")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        adapter = MyDeviceListAdapter( itemCallback = {
            topic = it.rfId
            adapter.setMessage(message = it.rfId)
            subscribe(topic)
        }, messageCallback =  {
            viewDataBinding.preview.setImageResource(it.image)
        })

        viewDataBinding.recyclerView.adapter = adapter
        adapter.submitList(messages)

        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        val normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)

        viewDataBinding.switchOnOff.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewDataBinding.textOn.typeface = boldTypeface
                viewDataBinding.textOff.typeface = normalTypeface
                viewDataBinding.statusIndicator.setImageResource(R.drawable.status_on)
                publishPayLoad(payload = "on", onTopic = topic)
            } else {
                viewDataBinding.textOff.typeface = boldTypeface
                viewDataBinding.textOn.typeface = normalTypeface
                viewDataBinding.statusIndicator.setImageResource(R.drawable.status_off)
                publishPayLoad(payload = "off", onTopic = topic)
            }
        }
    }

    private fun publishPayLoad(payload: String, onTopic: String) {
        val encodedPayload: ByteArray
        try {
            encodedPayload = payload.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            client?.publish(onTopic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String) {
        client?.subscribe(topic, 0, null, object : IMqttActionListener {
            @SuppressLint("LongLogTag")
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                //publishPayLoad(payload = "off", onTopic = topic)
                publishPayLoad(payload = "status", onTopic = topic)
                Log.e("Mqtt Status: ", "SUBSCRIBED!!")

            }

            @SuppressLint("LongLogTag")
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                exception?.message?.let { Log.e("MqttClient ERROR: ", it)}
            }
        })

    }
}