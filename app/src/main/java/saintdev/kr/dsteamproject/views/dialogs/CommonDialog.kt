package saintdev.kr.dsteamproject.views.dialogs

import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

object CommonDialog {
    fun openConfirm(title: String,
                    content: String,
                    activity: AppCompatActivity,
                    listener: OnDialogCanceledListener) {
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle(title)
        dialog.setMessage(content)
        dialog.setPositiveButton("네", DialogInterface.OnClickListener()
        {
            dialog, _ ->
            dialog.dismiss()
        })
    }

    fun openProgress(content: String, activity: AppCompatActivity) : ProgressDialog {
        val dialog = ProgressDialog(activity)
        dialog.setMessage(content)
        dialog.setCancelable(false)
        dialog.show()

        return dialog
    }
}

interface OnDialogCanceledListener {
    fun onPostive()     // 긍정적
    fun onNegative()    // 부정적
}

fun String.openMessage(title: String, activity: AppCompatActivity,
                       listener: DialogInterface.OnClickListener = DialogInterface.OnClickListener() { dialog, _ ->
    dialog.dismiss() })
{
    val dialog = AlertDialog.Builder(activity)
    dialog.setTitle(title)
    dialog.setMessage(this)
    dialog.setPositiveButton("확인", listener)
    dialog.show()
}