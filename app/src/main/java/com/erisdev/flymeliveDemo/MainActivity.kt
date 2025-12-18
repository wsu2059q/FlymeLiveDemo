package com.erisdev.flymeliveDemo

import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.Toast
import android.widget.RemoteViews
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.erisdev.flymelive.FlymeLiveManager
import com.erisdev.flymelive.showLiveNotification
import com.erisdev.flymelive.setCapsuleIconFromResource
import com.erisdev.flymelive.setCapsuleClickIntent
import com.erisdev.flymelive.setContentIntent
import com.erisdev.flymelive.setCapsuleContentColorRes
import com.erisdev.flymelive.setCapsuleBackgroundColorRes
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var flymeLiveManager: FlymeLiveManager
    
    // 参数输入框
    private lateinit var capsuleStatusInput: TextInputEditText
    private lateinit var capsuleTypeInput: TextInputEditText
    private lateinit var operationInput: TextInputEditText
    private lateinit var typeInput: TextInputEditText
    private lateinit var channelImportanceInput: TextInputEditText
    private lateinit var smallIconInput: TextInputEditText
    private lateinit var largeIconInput: TextInputEditText
    private lateinit var contentTitleInput: TextInputEditText
    private lateinit var contentTextInput: TextInputEditText
    private lateinit var capsuleContentInput: TextInputEditText
    private lateinit var extraKeyInput: TextInputEditText
    private lateinit var extraValueInput: TextInputEditText
    
    // 折叠相关控件
    private lateinit var toggleParamsButton: Button
    private lateinit var paramsContainer: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        flymeLiveManager = FlymeLiveManager(this)
        
        // 初始化折叠控件
        toggleParamsButton = findViewById(R.id.toggle_params_button)
        paramsContainer = findViewById(R.id.params_container)
        
        toggleParamsButton.setOnClickListener {
            if (paramsContainer.visibility == View.GONE) {
                paramsContainer.visibility = View.VISIBLE
                toggleParamsButton.text = "收起参数设置"
            } else {
                paramsContainer.visibility = View.GONE
                toggleParamsButton.text = "展开参数设置"
            }
        }
        
        // 初始化输入框
        capsuleStatusInput = findViewById(R.id.capsule_status_input)
        capsuleTypeInput = findViewById(R.id.capsule_type_input)
        operationInput = findViewById(R.id.operation_input)
        typeInput = findViewById(R.id.type_input)
        channelImportanceInput = findViewById(R.id.channel_importance_input)
        smallIconInput = findViewById(R.id.small_icon_input)
        largeIconInput = findViewById(R.id.large_icon_input)
        contentTitleInput = findViewById(R.id.content_title_input)
        contentTextInput = findViewById(R.id.content_text_input)
        capsuleContentInput = findViewById(R.id.capsule_content_input)
        extraKeyInput = findViewById(R.id.extra_key_input)
        extraValueInput = findViewById(R.id.extra_value_input)
        
        val showNotificationButton = findViewById<Button>(R.id.show_notification_button)
        showNotificationButton.setOnClickListener {
            showLiveNotificationExample()
        }
        
        // 取消通知按钮
        val cancelNotificationButton = findViewById<Button>(R.id.cancel_notification_button)
        cancelNotificationButton.setOnClickListener {
            cancelNotificationExample()
        }

        // 带点击事件的通知示例
        val showClickableNotificationButton = findViewById<Button>(R.id.show_clickable_notification_button)
        showClickableNotificationButton.setOnClickListener {
            showClickableLiveNotification()
        }
        
        // 参数测试按钮
        val testParamsButton = findViewById<Button>(R.id.test_params_button)
        testParamsButton.setOnClickListener {
            showParameterTestNotification()
        }

    }
    
    private fun showLiveNotificationExample() {
        try {
            // 创建胶囊视图
            val capsuleView = RemoteViews(packageName, R.layout.live_notification_capsule).apply {
                setTextViewText(R.id.capsule_content, "实况通知")
            }
            
            // 创建展开视图（同时也是通知栏显示的内容）
            val contentView = RemoteViews(packageName, R.layout.live_notification_content).apply {
                setTextViewText(R.id.title, "Flyme 实况通知")
                setTextViewText(R.id.message, "这是一条实况通知示例")
                setTextViewText(
                    R.id.description, 
                    "发送时间: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}"
                )
            }
            
            val notificationId = flymeLiveManager.showLiveNotification(
                channelId = "live_demo_channel",
                channelName = "Live Demo Channel",
                notificationId = 1001
            ) {
                // 在Flyme系统中，传统通知字段如标题、内容等通常不会显示
                // 只会显示胶囊和展开视图
                setSmallIcon(R.drawable.ic_notification)
                setContentTitle("这个标题在Flyme中不会显示")
                setContentText("这个内容在Flyme中也不会显示")
                
                // 设置胶囊相关属性
                setCapsuleContent("实况通知")
                setCapsuleContentView(capsuleView)
                setCapsuleIconFromResource(this@MainActivity, R.drawable.ic_notification)
                setCapsuleBgColor(android.R.color.holo_blue_bright)
                setCapsuleContentColor(android.R.color.white)
                
                // 设置展开视图（也是通知栏显示的内容）
                setContentView(contentView)
            }
            
            Toast.makeText(this, "已发送实况通知，ID: $notificationId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "发送通知失败: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    
    private fun cancelNotificationExample() {
        try {
            flymeLiveManager.cancelNotification(1001)
            flymeLiveManager.cancelNotification(1002)
            flymeLiveManager.cancelNotification(1003)
            flymeLiveManager.cancelNotification(1004)
            flymeLiveManager.cancelNotification(1005)
            Toast.makeText(this, "已取消所有示例通知", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "取消通知失败: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun showClickableLiveNotification() {
        try {
            // 创建带点击效果的胶囊视图
            val capsuleView = RemoteViews(packageName, R.layout.live_notification_capsule).apply {
                setTextViewText(R.id.capsule_content, "可点击")
            }
            
            // 创建带更多信息的展开视图
            val contentView = RemoteViews(packageName, R.layout.live_notification_content).apply {
                setTextViewText(R.id.title, "可交互实况通知")
                setTextViewText(R.id.message, "这条通知可以被点击")
                setTextViewText(
                    R.id.description, 
                    "点击通知可打开应用"
                )
            }
            
            val notificationId = flymeLiveManager.showLiveNotification(
                channelId = "clickable_channel",
                channelName = "Clickable Channel",
                notificationId = 1003
            ) {
                setSmallIcon(R.drawable.ic_notification)
                setContentTitle("可点击通知")
                setContentText("点击可查看详情")
                
                // 设置可点击样式
                setCapsuleContent("可点击")
                setCapsuleContentView(capsuleView)
                setCapsuleIconFromResource(this@MainActivity, R.drawable.ic_notification)
                setCapsuleBgColor(android.R.color.holo_orange_dark)
                setCapsuleContentColor(android.R.color.white)
                
                setContentView(contentView)

                // 点击事件
                setContentIntent(this@MainActivity, MainActivity::class.java)
                setCapsuleClickIntent(this@MainActivity, MainActivity::class.java)
                
                // 额外数据
                addExtra("click_action", "open_app")
                addExtra("timestamp", System.currentTimeMillis().toInt())
            }
            
            Toast.makeText(this, "已发送可点击实况通知，ID: $notificationId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "发送可点击通知失败: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun showParameterTestNotification() {
        try {
            // 获取输入的参数值，如果为空则使用默认值
            val capsuleStatus = capsuleStatusInput.text.toString().toIntOrNull() ?: 1
            val capsuleType = capsuleTypeInput.text.toString().toIntOrNull() ?: 5
            val operation = operationInput.text.toString().toIntOrNull() ?: 0
            val type = typeInput.text.toString().toIntOrNull() ?: 2
            val channelImportance = channelImportanceInput.text.toString().toIntOrNull() ?: NotificationManager.IMPORTANCE_DEFAULT
            val smallIcon = smallIconInput.text.toString().toIntOrNull() ?: R.drawable.ic_notification
            val largeIcon = largeIconInput.text.toString().toIntOrNull() ?: 0
            val contentTitle = contentTitleInput.text.toString().ifEmpty { "参数测试通知" }
            val contentText = contentTextInput.text.toString().ifEmpty { "测试不同参数值" }
            val capsuleContent = capsuleContentInput.text.toString().ifEmpty { "参数测试" }
            val extraKey = extraKeyInput.text.toString()
            val extraValue = extraValueInput.text.toString()
            
            // 创建胶囊视图
            val capsuleView = RemoteViews(packageName, R.layout.live_notification_capsule).apply {
                setTextViewText(R.id.capsule_content, capsuleContent)
            }
            
            // 创建展开视图
            val contentView = RemoteViews(packageName, R.layout.live_notification_content).apply {
                setTextViewText(R.id.title, contentTitle)
                setTextViewText(R.id.message, contentText)
                setTextViewText(
                    R.id.description,
                    "CapsuleStatus: $capsuleStatus, CapsuleType: $capsuleType\nOperation: $operation, Type: $type"
                )
            }
            
            val notificationId = flymeLiveManager.showLiveNotification(
                channelId = "param_test_channel",
                channelName = "Parameter Test Channel",
                notificationId = 1005
            ) {
                setSmallIcon(smallIcon)
                setContentTitle(contentTitle)
                setContentText(contentText)
                setImportance(channelImportance)
                
                // 设置胶囊相关属性
                setCapsuleContent(capsuleContent)
                setCapsuleStatus(capsuleStatus)
                setCapsuleType(capsuleType)
                setCapsuleContentView(capsuleView)
                setCapsuleIconFromResource(this@MainActivity, R.drawable.ic_notification)
                setCapsuleBgColor(android.R.color.holo_green_light)
                setCapsuleContentColor(android.R.color.black)
                
                // 设置展开视图
                setContentView(contentView)
                
                // 实况通知特定设置
                setOperation(operation)
                setType(type)
                
                // 额外参数（如果有输入）
                if (extraKey.isNotEmpty() && extraValue.isNotEmpty()) {
                    addExtra(extraKey, extraValue)
                }
                
                if (largeIcon != 0) {
                    setLargeIcon(largeIcon)
                }
            }
            
            Toast.makeText(
                this, 
                "已发送参数测试通知，ID: $notificationId\n" +
                "CapsuleStatus: $capsuleStatus, CapsuleType: $capsuleType\n" +
                "Operation: $operation, Type: $type", 
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "发送参数测试通知失败: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}