package com.example.online_shop

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.bottom_sheet_dialog_layout.view.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class BottomFragment(totalSum : String,
                     products : ArrayList<Product>,
                     orders : ArrayList<MyOrder>,
                     ctx: Context): BottomSheetDialogFragment() {

    private val displaySum = totalSum
    private val productList = products
    private var orderList = orders
    lateinit var db : FirebaseDatabase
    private val userEmail = Firebase.auth.currentUser?.email.toString().replace(".", " ")
    val ctxt = ctx

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);
        val btnPayNow = view.idPayNow
        val cardNumber = view.idCardNumber
        val holderName = view.idHolderName
        val cvv = view.idCvv
        val toPayTV = view.idToPayInDialog
        toPayTV.text = "To Pay: $displaySum"
        db = FirebaseDatabase.getInstance()

        btnPayNow.setOnClickListener {
            if(cardNumber.text.trim().toString().isNotEmpty() && holderName.text.trim().toString().isNotEmpty()
                && cvv.text.trim().toString().isNotEmpty()){
//                var userName = cardNumber.text.trim().toString();
                var ok = false

                if (cardNumber.text.length != 16) {
                    Toast.makeText(ctxt,"Card Number should contain 16 digits", Toast.LENGTH_LONG).show()
                } else {
                    if (cvv.text.length != 3) {
                        Toast.makeText(ctxt,"CVV should contain 3 digits", Toast.LENGTH_LONG).show()
                    } else {
                        ok = true
                    }
                }

                if (ok) {
                    val builder = AlertDialog.Builder(ctxt)
                    //set title for alert dialog
                    builder.setTitle("Confirm purchase")
                    //set message for alert dialog
                    builder.setMessage("shop address: Tole Bi 59")
                    builder.setIcon(R.drawable.ic_baseline_info_24)

                    //performing positive action
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                        Toast.makeText(ctxt,"clicked Ok",Toast.LENGTH_LONG).show()
                        // delete items from bucket
                        val dbBucketItems = db.getReference("bucket_items")
                        dbBucketItems.child(userEmail).removeValue()

                        // saving orders
                        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

                        val orderNumber = ThreadLocalRandom.current()
                            .ints(8, 0, charPool.size)
                            .asSequence()
                            .map(charPool::get)
                            .joinToString("")

                        val order = MyOrder(id = orderNumber,
                            orderNumber = orderNumber,
                            status = "Waiting",
                            products = productList)
                        orderList.add(order)

                        val dbMyOrders = db.getReference("my_orders")
                        dbMyOrders.child(userEmail).setValue(orderList)

                        dismiss()
                    }
                    //performing cancel action
                    builder.setNeutralButton("Cancel"){dialogInterface , which ->
                        Toast.makeText(ctxt,"clicked Cancel",Toast.LENGTH_LONG).show()
                    }
                    //performing negative action
                    builder.setNegativeButton("No"){dialogInterface, which ->
                        Toast.makeText(ctxt,"clicked No",Toast.LENGTH_LONG).show()
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }else{
                Toast.makeText(requireContext(),"All Fields Are Required", Toast.LENGTH_LONG).show()
            }
        }
        return view;
    }
}