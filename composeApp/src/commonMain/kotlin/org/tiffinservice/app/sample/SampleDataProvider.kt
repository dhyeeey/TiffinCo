package org.tiffinservice.app.sample

import org.tiffinservice.app.database.FoodEntity
import org.tiffinservice.app.database.RestaurantEntity
import org.tiffinservice.app.repository.TiffinRepository
import kotlin.random.Random

//object SampleDataProvider {
//
//    // --- REAL LOCATIONS (Ahmedabad & Gandhinagar) ---
//    private val restaurants = listOf(
//        // 1. Authentic Gujarati Thali Place (Ahmedabad)
//        RestaurantEntity(
//            name = "Gordhan Thal",
//            address = "Sapath Complex, S.G. Highway, Bodakdev",
//            city = "Ahmedabad",
//            state = "Gujarat",
//            thumbnailImageUrl = "https://images.unsplash.com/photo-1552590635-27c2c2128abf?auto=format&fit=crop&w=800&q=80",
//            rating = 4.8,
//            description = "The ultimate destination for authentic Gujarati Thali."
//        ),
//        // 2. Famous Snack Joint (Ahmedabad - Law Garden)
//        RestaurantEntity(
//            name = "Swati Snacks",
//            address = "Law Garden Cross Road, Ellisbridge",
//            city = "Ahmedabad",
//            state = "Gujarat",
//            thumbnailImageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
//            rating = 4.6,
//            description = "Iconic spot for Handvo, Panki, and traditional snacks."
//        ),
//        // 3. Gandhinagar Special (Infocity)
//        RestaurantEntity(
//            name = "Premwati Food Court",
//            address = "Akshardham Temple Complex, Sector 20",
//            city = "Gandhinagar",
//            state = "Gujarat",
//            thumbnailImageUrl = "https://images.unsplash.com/photo-1596450523292-b8830ef7d25e?auto=format&fit=crop&w=800&q=80",
//            rating = 4.9,
//            description = "Satvik and pure vegetarian food in the heart of Gandhinagar."
//        ),
//        // 4. Street Food Legend (Manek Chowk)
//        RestaurantEntity(
//            name = "Manek Chowk Night Eats",
//            address = "Manek Chowk, Old City, Khadia",
//            city = "Ahmedabad",
//            state = "Gujarat",
//            thumbnailImageUrl = "https://images.unsplash.com/photo-1567188040754-5835b2d2c626?auto=format&fit=crop&w=800&q=80",
//            rating = 4.5,
//            description = "Ahmedabad's legendary night street food market."
//        ),
//        // 5. Sweets & Farsan (Gandhinagar - Kudasan)
//        RestaurantEntity(
//            name = "Radhe Sweets",
//            address = "Pramukh Arcade, Kudasan Road",
//            city = "Gandhinagar",
//            state = "Gujarat",
//            thumbnailImageUrl = "https://images.unsplash.com/photo-1505253716362-afaea1d3d1af?auto=format&fit=crop&w=800&q=80",
//            rating = 4.4,
//            description = "Best place for Dhokla, Fafda, and fresh sweets."
//        )
//    )
//
//    private val foods = listOf(
//        // --- 1. Gordhan Thal (Authentic Meals) ---
//        FoodSeed(
//            1, "Gujarati Thali", "Unlimited thali with 4 sabzi, dal, kadhi & sweets", 350.0, "Thali",
//            "https://images.unsplash.com/photo-1594050963351-40489958dc18?auto=format&fit=crop&w=600&q=80" // Full Thali
//        ),
//        FoodSeed(
//            2, "Undhiyu", "Classic mixed vegetable delicacy of Surat", 180.0, "Sabzi",
//            "https://images.unsplash.com/photo-1589647365609-d36338e55e31?auto=format&fit=crop&w=600&q=80" // Veg Curry
//        ),
//        FoodSeed(
//            3, "Basundi", "Thickened sweetened milk with nuts", 120.0, "Dessert",
//            "https://images.unsplash.com/photo-1543773495668-3bd343c6b252?auto=format&fit=crop&w=600&q=80" // Creamy bowl (visually similar)
//        ),
//        FoodSeed(
//            4, "Dal Dhokli", "Spicy whole wheat pasta in dal", 140.0, "Main Course",
//            "https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=600&q=80" // Bowl of soup/curry
//        ),
//        FoodSeed(
//            5, "Aamras Puri", "Fresh mango pulp with fluffy pooris", 200.0, "Seasonal",
//            "https://images.unsplash.com/photo-1595982859188-755c3c06385d?auto=format&fit=crop&w=600&q=80" // Yellow pulp visual
//        ),
//
//        // --- 2. Swati Snacks (Traditional Light Food) ---
//        FoodSeed(
//            6, "Panki", "Rice pancake steamed in banana leaf", 110.0, "Snack",
//            "https://images.unsplash.com/photo-1604147705416-1392475dd8d3?auto=format&fit=crop&w=600&q=80" // Food in leaf
//        ),
//        FoodSeed(
//            7, "Handvo", "Savory fermented cake with bottle gourd", 130.0, "Snack",
//            "https://images.unsplash.com/photo-1504544750208-dc0358e63f7f?auto=format&fit=crop&w=600&q=80" // Savory cake/Pie slice
//        ),
//        FoodSeed(
//            8, "Satpadi Roti", "Spiced 7-grain flatbread with Gatta Sabzi", 160.0, "Meal",
//            "https://images.unsplash.com/photo-1565557623262-b51c2513a641?auto=format&fit=crop&w=600&q=80" // Roti/Paratha visual
//        ),
//        FoodSeed(
//            9, "Fada Ni Khichdi", "Broken wheat khichdi with spices", 150.0, "Healthy",
//            "https://images.unsplash.com/photo-1596560548464-f010549b84d7?auto=format&fit=crop&w=600&q=80" // Grain/Rice bowl
//        ),
//        FoodSeed(
//            10, "Sugarcane Juice", "Freshly pressed with ginger and lemon", 60.0, "Beverage",
//            "https://images.unsplash.com/photo-1620916297397-a4a5402a3c6c?auto=format&fit=crop&w=600&q=80" // Green Juice
//        ),
//
//        // --- 3. Premwati (Gandhinagar Pure Veg) ---
//        FoodSeed(
//            11, "Swaminarayan Khichdi", "Light khichdi with ghee and kadhi", 120.0, "Sattvic",
//            "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=600&q=80" // Comfort food/Rice
//        ),
//        FoodSeed(
//            12, "Masala Dosa", "Crispy dosa with potato bhaji", 100.0, "South Indian",
//            "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=600&q=80" // Authentic Dosa
//        ),
//        FoodSeed(
//            13, "Rajwadi Chaas", "Spiced buttermilk", 30.0, "Beverage",
//            "https://images.unsplash.com/photo-1600624029272-3c22b9322b2b?auto=format&fit=crop&w=600&q=80" // White drink glass
//        ),
//        FoodSeed(
//            14, "Veg Pulao", "Rice with peas and carrots", 110.0, "Rice",
//            "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=600&q=80" // Pulao/Biryani
//        ),
//        FoodSeed(
//            15, "Sukhdi", "Wheat flour and jaggery sweet", 50.0, "Sweet",
//            "https://images.unsplash.com/photo-1599260171887-1975e526433e?auto=format&fit=crop&w=600&q=80" // Brown fudge/sweet
//        ),
//
//        // --- 4. Manek Chowk (Street Food) ---
//        FoodSeed(
//            16, "Pav Bhaji", "Amul butter loaded bhaji pav", 160.0, "Street Food",
//            "https://images.unsplash.com/photo-1606491956689-2ea28c674675?auto=format&fit=crop&w=600&q=80" // Perfect Pav Bhaji
//        ),
//        FoodSeed(
//            17, "Chocolate Sandwich", "Grilled sandwich with chocolate chips", 120.0, "Dessert",
//            "https://images.unsplash.com/photo-1628191010210-a59de33e5941?auto=format&fit=crop&w=600&q=80" // Grilled Sandwich
//        ),
//        FoodSeed(
//            18, "Gwalior Dosa", "Butter dosa with cheese", 180.0, "Fusion",
//            "https://images.unsplash.com/photo-1630384060421-a4323ceca5f6?auto=format&fit=crop&w=600&q=80" // Loaded Dosa
//        ),
//        FoodSeed(
//            19, "Kulfi Falooda", "Rabdi kulfi with vermicelli", 90.0, "Dessert",
//            "https://images.unsplash.com/photo-1579954115563-e72bf1381629?auto=format&fit=crop&w=600&q=80" // Ice cream stick
//        ),
//        FoodSeed(
//            20, "Cheese Pineapple Sandwich", "Ahmedabad special fruit sandwich", 140.0, "Street Food",
//            "https://images.unsplash.com/photo-1554433607-66b5efe9d304?auto=format&fit=crop&w=600&q=80" // Sandwich
//        ),
//
//        // --- 5. Radhe Sweets (Farsan) ---
//        FoodSeed(
//            21, "Nylon Khaman", "Soft and spongy yellow cakes", 60.0, "Farsan",
//            "https://images.unsplash.com/photo-1550950158-d0d960d9f9dd?auto=format&fit=crop&w=600&q=80" // Yellow sponge visual
//        ),
//        FoodSeed(
//            22, "Khandvi", "Rolled gram flour spiced snacks", 70.0, "Farsan",
//            "https://images.unsplash.com/photo-1546250785-f5546263599e?auto=format&fit=crop&w=600&q=80" // Rolled yellow snack
//        ),
//        FoodSeed(
//            23, "Fafda Jalebi", "Sunday breakfast special combo", 100.0, "Breakfast",
//            "https://images.unsplash.com/photo-1631899147496-c6511b025f82?auto=format&fit=crop&w=600&q=80" // Jalebi visual
//        ),
//        FoodSeed(
//            24, "Samosa", "Crispy potato stuffed triangle", 40.0, "Snack",
//            "https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=600&q=80" // Samosa
//        ),
//        FoodSeed(
//            25, "Kaju Katli", "Diamond shaped cashew fudge", 250.0, "Sweet",
//            "https://images.unsplash.com/photo-1515037028865-0a2a82603f7c?auto=format&fit=crop&w=600&q=80" // Diamond sweets
//        )
//    )
//
//    suspend fun insertAllSampleData(repo: TiffinRepository) {
//        val restaurantIds = restaurants.map { repo.insertRestaurant(it) }
//
//        // Logic ensures 5 foods are assigned to each restaurant
//        foods.chunked(5).forEachIndexed { index, group ->
//            // Use 'getOrNull' to prevent crashes if math is slightly off, though here it matches
//            val restaurantId = restaurantIds.getOrNull(index) ?: return@forEachIndexed
//
//            group.forEach { seed ->
//                repo.insertFood(
//                    FoodEntity(
//                        // If your database auto-generates IDs, you might want to set this to 0
//                        // depending on your Room configuration.
//                        // If 'food_id' is mapped from seed to keep order, keep as is.
//                        food_id = seed.foodId,
//                        restaurant_id = restaurantId,
//                        name = seed.name,
//                        description = seed.description,
//                        category = seed.category,
//                        price = seed.price,
//                        imageUrl = seed.imageUrl,
//                        rating = Random.nextDouble(3.8, 4.9)
//                    )
//                )
//            }
//        }
//    }
//}

object SampleDataProvider {

    // --- AUTHENTIC TIFFIN SERVICES (Ahmedabad & Gandhinagar) ---
    private val restaurants = listOf(
        // 1. Traditional Gujarati Tiffin (Ahmedabad - Naranpura)
        RestaurantEntity(
            name = "Flavor Fusions",
            address = "Naranpura Char Rasta, Naranpura Vistar",
            city = "Ahmedabad",
            state = "Gujarat",
            thumbnailImageUrl = "https://indianbusinesscanada.com/admin/uploads/2021/04/09/pure-vegetarian-indian-gujarati-tiffin-service-scarborough-82862-1-26.jpeg",
            rating = 4.8,
            description = "Authentic homemade Gujarati tiffin service with pure vegetarian meals."
        ),
        // 2. Premium Tiffin Service (Ahmedabad - Bodakdev)
        RestaurantEntity(
            name = "Annapurna Tiffin Service",
            address = "Riddhishwer Society, Naranpura, Bodakdev",
            city = "Ahmedabad",
            state = "Gujarat",
            thumbnailImageUrl = "https://tse1.mm.bing.net/th/id/OIP.YuddyTb6eiahGX7xkUmAKQHaHW?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3",
            rating = 4.9,
            description = "Premium daily lunch boxes with nutritious homemade meals."
        ),
        // 3. Gandhinagar Tiffin Hub (Gandhinagar - Infocity)
        RestaurantEntity(
            name = "Patel Tiffin Service",
            address = "Sector 20, Infocity, Gandhinagar",
            city = "Gandhinagar",
            state = "Gujarat",
            thumbnailImageUrl = "https://media.istockphoto.com/id/666193036/photo/indian-vegetarian-office-or-school-lunch-box-or-tiffin-with-north-indian-or-maharashtrian.jpg?s=612x612&w=0&k=20&c=5Ql8CEusNPK2r37cKyi11Y2C2PTf3rqq_pGH6G4iYiM=",
            rating = 4.7,
            description = "Specialized tiffin service for working professionals and students."
        ),
        // 4. Quality Tiffin Venture (Ahmedabad - Naranpura)
        RestaurantEntity(
            name = "Deepti's Delicious Delights",
            address = "Rajkrupa Flats, Near Mithakhali, Navrangpura",
            city = "Ahmedabad",
            state = "Gujarat",
            thumbnailImageUrl = "https://st4.depositphotos.com/19960896/27842/i/450/depositphotos_278423044-stock-photo-indian-food-platter-hindu-veg.jpg",
            rating = 4.6,
            description = "Carefully prepared tiffin meals with balanced nutrition."
        ),
        // 5. Gandhinagar Daily Tiffin (Gandhinagar - Kudasan)
        RestaurantEntity(
            name = "The Tiffin Table",
            address = "Near Himalaya Mall, Drive In Road, Gandhinagar",
            city = "Gandhinagar",
            state = "Gujarat",
            thumbnailImageUrl = "https://5.imimg.com/data5/SELLER/Default/2023/5/307337641/UH/EH/GF/189562155/indian-thali-jpeg-500x500.jpeg",
            rating = 4.5,
            description = "Reliable tiffin service with consistent quality and taste."
        )
    )

    private val foods = listOf(
        // --- 1. Maa Ka Swad (Traditional Gujarati Tiffin) ---
        FoodSeed(
            1, "Full Tiffin - Roti, Dal & Sabzi", "5 rotlis with 2 curries, dal, rice, chaas & salad", 120.0, "Main Course",
            "https://img.pikbest.com/photo/20250514/-a-colorful-indian-tiffin-lunch-with-roti-sabzi-rice-and-pickles-on-wooden-table_11715468.jpg!bw700"
        ),
        FoodSeed(
            2, "Jeera Rice with Undhiyu", "Fragrant jeera rice with Gujarati undhiyu curry", 150.0, "Rice Main",
            "https://tse3.mm.bing.net/th/id/OIP.uIyNWj_TIYFx2PYCBla5NQHaGf?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            3, "Dal Dhokli", "Spicy whole wheat pasta in dal with ghee tadka", 100.0, "Comfort Food",
            "https://th.bing.com/th/id/R.f1bdac27436018c86b71f9dc2786e3b7?rik=ie3n48o7dEFKCA&riu=http%3a%2f%2fwww.spiceupthecurry.com%2fwp-content%2fuploads%2f2013%2f09%2fDal-Dhokli-Gujarati-Recipe.jpg&ehk=LMM0uFO%2b%2bDPHxTlkIbF2FZvtLsNEXehNPudLqT%2bTAx8%3d&risl=&pid=ImgRaw&r=0"
        ),
        FoodSeed(
            4, "Bajra & Rotli Combo", "Bajra roti with butter & Gatta sabzi", 110.0, "Millet Special",
            "https://img-global.cpcdn.com/recipes/b778ada338da960b/680x482cq70/bajra-no-rotlolasaniya-ringada-nu-shaakkhichadi-upar-gheechaasonionmarcha-kathiyawadi-dinner-recipe-main-photo.jpg"
        ),
        FoodSeed(
            5, "Buttermilk & Khichdi", "Warm khichdi with spiced buttermilk", 90.0, "Light & Healthy",
            "https://www.cookingwithsiddhi.com/wp-content/uploads/2019/11/gujarati-khichdi-recipe.jpg"
        ),

        // --- 2. Annapurna Tiffin Service (Premium Meals) ---
        FoodSeed(
            6, "Jowar Roti with Methi Sabzi", "Jowar rotli with fresh fenugreek curry", 105.0, "Whole Grain",
            "https://tse3.mm.bing.net/th/id/OIP.uZXrqrTGQwGQDZW4m8y9HwHaE8?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            7, "Toor Dal with Bhaat", "Protein-rich toor dal with fluffy rice", 95.0, "Dal Rice",
            "https://tse3.mm.bing.net/th/id/OIP.mdlY3wtIm3MOfFBTXjtzsQHaEK?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            8, "Masoor Dal & Wheat Roti", "Light and nutritious masoor dal with wheat bread", 85.0, "Light Meal",
            "https://tse2.mm.bing.net/th/id/OIP.HkMrme31UA6gQ-tBMVxGEwHaFk?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            9, "Vegetable Pulao", "Mixed vegetable pulao with yogurt and pickle", 110.0, "Rice Specialty",
            "https://tse3.mm.bing.net/th/id/OIP.VaIEUducWRYY61lKeRyBTQHaHa?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            10, "Chikhalwali Rotli", "Traditional layered rotli with ghee", 75.0, "Traditional",
            "https://tse4.mm.bing.net/th/id/OIP.a4ZUX3OI342K-86o8LyZKQHaLH?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),

        // --- 3. Patel Tiffin Service (Student & Professional Meals) ---
        FoodSeed(
            11, "Budget Tiffin - 3 Roti Combo", "3 rotlis with 1 curry, dal and rice", 80.0, "Budget Meal",
            "https://tse2.mm.bing.net/th/id/OIP.1dCakbcTKxnNFurMZ1ZbcwHaE8?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            12, "Rotli with Potato Curry", "5 rotlis with spiced potato sabzi", 90.0, "Potato Special",
            "https://tse1.explicit.bing.net/th/id/OIP.PYcgZ_Yps6zv2rtqr0cPNQHaE8?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            13, "Spinach & Cottage Cheese", "Palak paneer with wheat roti", 125.0, "Protein Meal",
            "https://tse3.mm.bing.net/th/id/OIP.X_Q0pHCBiANwk2-2QBhUUAHaE8?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            14, "Moong Dal Khichdi", "Comforting moong dal khichdi with ghee", 85.0, "Easy Digest",
            "https://tse2.mm.bing.net/th/id/OIP.1WpFL1O2DkVzHgfMvndy4gHaHa?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            15, "Rajma Rice", "Kidney beans curry with steamed rice", 100.0, "Rice Curry",
            "https://tse2.mm.bing.net/th/id/OIP.jmR4E-ZeGCy9CPcQSfIpagHaEJ?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),

        // --- 4. Deepti's Delicious Delights (Balanced Nutrition) ---
        FoodSeed(
            16, "Mixed Vegetable Thali", "Rotli, 2 sabzi, dal, rice, chaas & salad", 130.0, "Complete Meal",
            "https://img.freepik.com/premium-photo/indian-thali-variety-dishes-including-dal-sabzi-roti-rice-sweet-gulab-jamun-beautifully-presented_1266280-97063.jpg?w=2000"
        ),
        FoodSeed(
            17, "Cauliflower & Pea Curry", "Gobi mutter masala with 4 rotlis", 95.0, "Vegetable Main",
            "https://tse1.mm.bing.net/th/id/OIP.MQW1ztGj8wtLKzEWngnYkAHaFZ?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            18, "Pearl Millet Rotli Meal", "Bajri rotli with tomato curry and curd", 105.0, "Millet Specialty",
            "https://ministryofcurry.com/wp-content/uploads/2022/10/Bajri-Bhakri_-5.jpg"
        ),
        FoodSeed(
            19, "Arhar Dal with Rice", "Pigeon pea dal cooked in traditional style", 90.0, "Dal Special",
            "https://tse2.mm.bing.net/th/id/OIP.5zQDNxoC2wlx3K1A_UMsXQHaEK?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            20, "Fenugreek Paratha Combo", "Methi paranthe with pickle and yogurt", 110.0, "Paratha Special",
            "https://tse1.mm.bing.net/th/id/OIP.ske8915YmWaCJFlNNlx4WwHaE-?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),

        // --- 5. Amrut Tiffin Services (Reliable Daily Service) ---
        FoodSeed(
            21, "Simple Dal Rice Meal", "Yellow dal with plain rice and vegetables", 75.0, "Daily Essential",
            "https://c8.alamy.com/comp/2CD7TCF/indian-meal-homemade-of-rice-dal-and-curry-with-pickle-ready-to-be-served-2CD7TCF.jpg"
        ),
        FoodSeed(
            22, "Rotli with Brinjal Curry", "4 rotlis with eggplant sabzi", 85.0, "Vegetable Meal",
            "https://tse3.mm.bing.net/th/id/OIP.u34bXMUcvGDxacryAqV-xwHaFj?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            23, "Chickpea Curry & Bhaat", "Kabuli chana with steamed rice", 95.0, "Protein Rich",
            "https://tse3.mm.bing.net/th/id/OIP.wxMVqUiFoH5tO7kW1-_riAHaLI?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            24, "Bottle Gourd Curry", "Dudhi sabzi with 5 wheat rotlis", 80.0, "Light Curry",
            "https://tse4.mm.bing.net/th/id/OIP.vmha7bdOGKLIP_hsraDoswHaLG?cb=ucfimg2&ucfimg=1&w=735&h=1102&rs=1&pid=ImgDetMain&o=7&rm=3"
        ),
        FoodSeed(
            25, "Corn & Vegetable Rice", "Maize rice with mixed vegetables", 100.0, "Corn Special",
            "https://tse4.mm.bing.net/th/id/OIP.Wdk_LrZx850NpDuqxdOulAHaHa?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
        )
    )

//    suspend fun insertAllSampleData(repo: TiffinRepository) {
//
//        val restaurantIds = repo.insertAllRestaurants(restaurants)
//
//        // Logic ensures 5 foods are assigned to each tiffin service
//        foods.chunked(5).forEachIndexed { index, group ->
//            // Use 'getOrNull' to prevent crashes if math is slightly off
//            val restaurantId = restaurantIds.getOrNull(index) ?: return@forEachIndexed
//
//            group.forEach { seed ->
//                repo.insertFood(
//                    FoodEntity(
//                        food_id = seed.foodId,
//                        restaurant_id = restaurantId,
//                        name = seed.name,
//                        description = seed.description,
//                        category = seed.category,
//                        price = seed.price,
//                        imageUrl = seed.imageUrl,
//                        rating = Random.nextDouble(3.8, 4.9)
//                    )
//                )
//            }
//        }
//    }

    suspend fun insertAllSampleData(repo: TiffinRepository) {

        repo.insertAllRestaurants(restaurants)

        val savedRestaurants = repo.getAllRestaurants()

        if (savedRestaurants.isEmpty()) return

        foods.chunked(5).forEachIndexed { index, group ->

            val restaurant = savedRestaurants.getOrNull(index) ?: return@forEachIndexed

            group.forEach { seed ->

                repo.insertFood(
                    FoodEntity(
                        restaurant_id = restaurant.restaurant_id,
                        name = seed.name,
                        description = seed.description,
                        category = seed.category,
                        price = seed.price,
                        imageUrl = seed.imageUrl,
                        rating = Random.nextDouble(3.8, 4.9)
                    )
                )
            }
        }
    }

}


data class FoodSeed(

    val foodId: Long,

    val name: String,

    val description: String,

    val price: Double,

    val category: String,

    val imageUrl: String

)

