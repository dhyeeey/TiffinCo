package org.tiffinservice.app.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tiffinservice.app.DTO.MenuItem

class MenuRepository {

    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems = _menuItems.asStateFlow()

    init {
        // Simulated static data; later can be loaded from network
        _menuItems.value = listOf(
            MenuItem(
                1, "Combo", "Rajma Chawal Combo",
                "A wholesome meal with spiced kidney beans, steamed rice, and a side salad.",
                200.0,
                "https://lh3.googleusercontent.com/aida-public/AB6AXuBUrsGpYlLsimQ5CAOEpzeNjGb91bgdGLTEmV1iS1iOI488YqnViL1bRzXQh8CBV0rSIgbmCPv4kGUxPhhvlqCK-QQN0eWOvn5HbQP2E9ScVvUnnOq52xjB_mQk59OY17A6O18Is9gSnAuKYQlMhxZ_QR63y3nv5wmKV6OITU6fBVn8Nf3vfQEsPFoRLUxxhEz1n1STRjAbiuG952KMWws_KQ9Ni3_85q4sTURe8MDiHVXbCud9zarE0NDpeltm8NnxIcAEXGgbFYE"
            ),
            MenuItem(
                2, "Thali", "Paneer Butter Masala Thali",
                "Creamy paneer curry served with two rotis, dal, and rice.",
                350.0,
                "https://lh3.googleusercontent.com/aida-public/AB6AXuBHN8Yf87MBrrWJfRtkpz2ukgWpr0M-9vFxVokNvsfTYI1rtTr2mqvYOfo48S7R23FY2DPL2bQKoFl8cLU2tLnJVvIOibDMFB3r14J8CC3xNr9pvHJyXX8VvbzFQEEMRZz0ZKA8FWXf445uBbr9yUFo9prvpUZKaDVKd-pjq3Kr_Pd5mq4KD2CtykMvEyISIafoUukp8wOm3lYFcY1fi981HWQ_87b6QYEFB5cIToTXEqHvc_wt7tXawQMNhZdDnZLTW4NwvgUl7i0"
            ),
            MenuItem(
                3, "Special", "Vegetable Biryani",
                "Fragrant basmati rice cooked with mixed vegetables and aromatic spices.",
                170.0,
                "https://lh3.googleusercontent.com/aida-public/AB6AXuBLd51eYwdTPsMA5sJOCk9GBf41Dmo9hAizeidclM4yQHzbkfVfcFk00lOPoPYdJkUvbclrc8OxSygYFWnw87XXz5FDX3rjCK5tvWyo2btVqlL_hfgVBlugrIkGkoCd6csmV2bafFpEgPQvn6YN9D0hSnl3njQFbvq3So52aT_BRfUd7w1BFRYl0tm_vIHEPP26972uY2ABICtmIMQe0xdqsWqr4uYdpaypAzpLjsyNgdGICO0L5ZBXn6tW-V_bDa3Lo9vABiuITbE"
            )
        )
    }

    fun getItemById(id: Int): MenuItem? = _menuItems.value.find { it.id == id }
}
