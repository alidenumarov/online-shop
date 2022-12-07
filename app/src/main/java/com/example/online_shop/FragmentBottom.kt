package com.example.online_shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.bottom_sheet_dialog_layout.view.*

class BottomFragment(totalSum : String): BottomSheetDialogFragment() {

    private val displaySum = totalSum

    @SuppressLint("SetTextI18n")
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

        btnPayNow.setOnClickListener {
            val user = Firebase.auth.currentUser
            Toast.makeText(requireContext(),user?.email.toString(), Toast.LENGTH_LONG).show()


            if(cardNumber.text.trim().toString().isNotEmpty() && holderName.text.trim().toString().isNotEmpty()
                && cvv.text.trim().toString().isNotEmpty()){
                var userName = cardNumber.text.trim().toString();
                Toast.makeText(requireContext(),userName, Toast.LENGTH_LONG).show()
//                startActivity(Intent(requireContext(), ItemActivity::class.java).putExtra("data",userName))
//                dismiss()
            }else{
                Toast.makeText(requireContext(),"All Fields Are Required", Toast.LENGTH_LONG).show()
            }
        }
        return view;
    }
}