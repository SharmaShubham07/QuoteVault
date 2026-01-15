package com.example.brewappsassignment.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.example.brewappsassignment.R
import com.example.brewappsassignment.data.remote.SupabaseClient
import com.example.brewappsassignment.data.remote.QuotesApi
import com.example.brewappsassignment.domain.repository.QuoteRepository
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.ui.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuoteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, manager, appWidgetIds)

        for (widgetId in appWidgetIds) {
            updateQuoteWidget(context, manager, widgetId)
        }
    }

    companion object {

        fun updateQuoteWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_quote)

            // Show loading text
            views.setTextViewText(R.id.tvQuote, "Updating...")
            manager.updateAppWidget(widgetId, views)

            val repo = QuoteRepository(
                SupabaseClient.instance.create(QuotesApi::class.java),
                SupabaseClient.instance.create(com.example.brewappsassignment.data.remote.FavoritesApi::class.java),
                SessionManager(context)
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val qotd = repo.getRandomQOTD()

                    Handler(Looper.getMainLooper()).post {
                        views.setTextViewText(R.id.tvQuote, "\"${qotd.text}\"")
                        views.setTextViewText(R.id.tvAuthor, "— ${qotd.author}")

                        // Open app on click
                        val intent = Intent(context, HomeActivity::class.java)
                        val pendingIntent = PendingIntent.getActivity(
                            context, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)

                        manager.updateAppWidget(widgetId, views)
                    }

                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        views.setTextViewText(R.id.tvQuote, "Failed to load quote")
                        views.setTextViewText(R.id.tvAuthor, "")
                        manager.updateAppWidget(widgetId, views)
                    }
                }
            }
        }

        fun forceUpdate(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, QuoteWidget::class.java)
            val widgetIds = manager.getAppWidgetIds(component)

            for (id in widgetIds) updateQuoteWidget(context, manager, id)
        }
    }
}
