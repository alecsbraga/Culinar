package com.example.culinar.ui.home.Food

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.culinar.MainActivity
import com.example.culinar.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailsFood : AppCompatActivity() {

    private lateinit var buttonShare: Button
    private var shouldShowNotifications = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food)
        setupDetailsFood()
        setupBackButtonMenu()
    }

    private fun setupBackButtonMenu() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view_back)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_back_home -> {
                    shouldShowNotifications = false
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setupDetailsFood() {
        val food = intent.getParcelableExtra<Food>("food")
        val titleView: TextView = findViewById(R.id.titleView)
        val imageView: ImageView = findViewById(R.id.imageViewDetails)
        val detailsText: TextView = findViewById(R.id.textDetailsView)

        if (food != null) {
            titleView.text = food.name
            imageView.setImageResource(food.image)
            detailsText.text = food.text
        } else {
            val name = intent.getStringExtra("name")
            if (name != null) {
                titleView.text = name
            }
            val image = intent.getStringExtra("image")
            if (image != null) {
                imageView.setImageResource(image.toInt())
            }
            val text = intent.getStringExtra("text")
            if (text != null) {
                detailsText.text = text
            }
        }

        detailsText.movementMethod  = ScrollingMovementMethod()
        setupButtonShare()
    }

    private fun setupButtonShare() {
        val food = intent.getParcelableExtra<Food>("food")
        buttonShare = findViewById(R.id.shareButton)

        setupAnimationButton(buttonShare)

        val text =
            "The best food  "+ food?.name +" and more delicious recipes, can be found in our application. Try it, the best App: www.culinar.com"

        buttonShare.setOnClickListener {
            shouldShowNotifications = false
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Share using..."))
        }

    }

    private fun setupAnimationButton(buttonShare: Button) {
        val rotateAnimation = ObjectAnimator.ofFloat(buttonShare, "rotation", 0f, 5f, -5f, 5f, -5f, 0f)
        rotateAnimation.duration = 1000
        rotateAnimation.repeatCount = ObjectAnimator.INFINITE

        val translateAnimation = ObjectAnimator.ofFloat(buttonShare, "translationY", 0f, -10f, 0f, 10f, 0f)
        translateAnimation.duration = 500
        translateAnimation.repeatCount = ObjectAnimator.INFINITE

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotateAnimation, translateAnimation)
        animatorSet.start()
    }

    private fun showNotificationWithDeepLink(context: Context, deepLink: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "my_channel_id",
            "My Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val uri = Uri.parse(deepLink)

        val name = uri.getQueryParameter("name")
        val image = uri.getQueryParameter("image")
        val text = uri.getQueryParameter("text")


        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.putExtra("name", name)
        intent.putExtra("image", image)
        intent.putExtra("text", text)


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, "my_channel_id")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Come back from here")
            .setContentText("Mmm, mmm! Did you forget any recipe?")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }


    override fun onUserLeaveHint() {
        if(shouldShowNotifications == true){
            val food = intent.getParcelableExtra<Food>("food")
            val deeplink = "culinar://details-food?name=" + (food?.name ?: "") + "&image=" + (food?.image ?: 0) + "&text=" + (food?.text?: "")
            showNotificationWithDeepLink(this, deeplink)
        }

    }

}