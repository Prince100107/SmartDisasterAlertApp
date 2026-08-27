package com.example.smartdisasteralert.data.local

import com.example.smartdisasteralert.R
import com.example.smartdisasteralert.data.model.DisasterAlert
import com.example.smartdisasteralert.data.model.DisasterType
import com.example.smartdisasteralert.data.model.EmergencyContact
import com.example.smartdisasteralert.data.model.SafetyTip
import com.example.smartdisasteralert.data.model.SeverityLevel

object MockDisasterDataSource {

    fun getFallbackDisasters(): List<DisasterAlert> {
        val now = System.currentTimeMillis()
        return listOf(
            DisasterAlert(
                id = "alert_eq_01",
                title = "Earthquake M 5.6",
                type = DisasterType.EARTHQUAKE,
                severity = SeverityLevel.HIGH,
                locationName = "Near Bhuj, Kutch, Gujarat",
                latitude = 23.2420,
                longitude = 69.6669,
                timestamp = now - (18 * 60 * 1000), // 18 mins ago
                description = "Moderate to strong seismic tremor registered at a depth of 12 km. Structural inspection recommended for older masonry buildings.",
                safetyInstructions = "• Drop, Cover, and Hold On immediately.\n• Extinguish open flames and turn off gas valves.\n• Avoid brick walls, power lines, and overhangs.\n• Check for injuries once shaking stops.",
                source = "USGS Seismic Network",
                magnitudeOrMetric = "Magnitude 5.6 | Depth 12km",
                affectedRadiusKm = 45
            ),
            DisasterAlert(
                id = "alert_rain_02",
                title = "Flash Flood & Heavy Rainfall Warning",
                type = DisasterType.FLOOD,
                severity = SeverityLevel.CRITICAL,
                locationName = "Narmada Basin & Coastal Saurashtra",
                latitude = 21.7051,
                longitude = 72.9959,
                timestamp = now - (42 * 60 * 1000),
                description = "Continuous severe rainfall exceeding 180mm recorded in 6 hours. Rivers and low-lying underpasses experiencing rapid water logging.",
                safetyInstructions = "• Move immediately to higher ground.\n• Do not attempt to drive or walk through flooded roadways.\n• Disconnect electrical appliances.\n• Keep emergency kit and battery torch accessible.",
                source = "State Disaster Management Authority",
                magnitudeOrMetric = "Rainfall > 180mm | Flash Flood Risk",
                affectedRadiusKm = 70
            ),
            DisasterAlert(
                id = "alert_cyclone_03",
                title = "Severe Cyclonic Storm Alert",
                type = DisasterType.CYCLONE,
                severity = SeverityLevel.HIGH,
                locationName = "Arabian Sea Coast, Porbandar",
                latitude = 21.6417,
                longitude = 69.6293,
                timestamp = now - (2 * 3600 * 1000),
                description = "Cyclonic circulation intensifying with sustained surface wind gusts up to 110 km/h. High tidal waves and heavy squalls forecast.",
                safetyInstructions = "• Fishermen advised not to venture into the sea.\n• Secure corrugated tin roofs and window panes.\n• Keep drinking water and non-perishable food stored.\n• Stay tuned to weather radio bulletins.",
                source = "IMD Coastal Warning Bulletin",
                magnitudeOrMetric = "Wind Speed 110 km/h | Category 2",
                affectedRadiusKm = 120
            ),
            DisasterAlert(
                id = "alert_thunder_04",
                title = "Intense Thunderstorm & Lightning Warning",
                type = DisasterType.THUNDERSTORM,
                severity = SeverityLevel.MEDIUM,
                locationName = "Ahmedabad & Gandhinagar Region",
                latitude = 23.0225,
                longitude = 72.5714,
                timestamp = now - (35 * 60 * 1000),
                description = "Frequent cloud-to-ground lightning flashes accompanied by gusty winds up to 55 km/h. Urban localized ponding expected.",
                safetyInstructions = "• Seek shelter inside a substantial building or hardtop vehicle.\n• Unplug sensitive electronics.\n• Avoid standing near solitary tall trees or metal fences.\n• Postpone outdoor recreational activities.",
                source = "Regional Doppler Radar",
                magnitudeOrMetric = "Frequent Lightning | Wind 55 km/h",
                affectedRadiusKm = 30
            ),
            DisasterAlert(
                id = "alert_fire_05",
                title = "Wildfire Outbreak Risk",
                type = DisasterType.FIRE,
                severity = SeverityLevel.MEDIUM,
                locationName = "Gir Forest Fringe, Junagadh",
                latitude = 21.1444,
                longitude = 70.8242,
                timestamp = now - (5 * 3600 * 1000),
                description = "Dry shrub vegetation fire spreading due to high ambient temperatures and dry wind currents. Forest rangers active in firebreaks.",
                safetyInstructions = "• Maintain defensible space around farm structures.\n• Close all doors, vents, and windows to block smoke and embers.\n• Have evacuation plan ready if directed by local authorities.\n• Keep garden hoses connected.",
                source = "Forest Emergency Response",
                magnitudeOrMetric = "Active Fire Perimeter 4 km²",
                affectedRadiusKm = 25
            ),
            DisasterAlert(
                id = "alert_rain_06",
                title = "Persistent Torrential Downpour",
                type = DisasterType.RAIN,
                severity = SeverityLevel.LOW,
                locationName = "Surat & Tapi River Catchment",
                latitude = 21.1702,
                longitude = 72.8311,
                timestamp = now - (7 * 3600 * 1000),
                description = "Steady precipitation over the last 12 hours with moderate water discharge. Inundation localized to stormwater drains.",
                safetyInstructions = "• Drive with reduced speeds and headlights on.\n• Watch out for open manholes and flooded ditches.\n• Keep cellular phones charged.",
                source = "Municipal Flood Cell",
                magnitudeOrMetric = "Rain Accumulation: 65mm",
                affectedRadiusKm = 35
            )
        )
    }

    fun getSafetyTips(): List<SafetyTip> {
        return listOf(
            SafetyTip(
                id = "tip_eq",
                disasterType = DisasterType.EARTHQUAKE,
                title = "Earthquake Preparedness",
                subtitle = "Drop, Cover, and Hold On Protocols",
                beforeSteps = listOf(
                    "Fasten heavy furniture, cupboards, and water heaters securely to wall studs.",
                    "Practice earthquake drills with family: locate safe spots under heavy desks.",
                    "Keep a survival pack with at least 3 days of water, food, and first-aid supplies."
                ),
                duringSteps = listOf(
                    "DROP to hands and knees so you are not knocked down.",
                    "COVER your head and neck under a sturdy table or desk.",
                    "HOLD ON to your shelter until shaking completely stops.",
                    "If outdoors, move away from buildings, streetlights, and utility wires."
                ),
                afterSteps = listOf(
                    "Check yourself and surrounding people for injuries; provide first aid.",
                    "Expect aftershocks which can cause further damage to weakened structures.",
                    "Inspect gas lines for leaks; smell gas? Open windows and evacuate immediately."
                )
            ),
            SafetyTip(
                id = "tip_flood",
                disasterType = DisasterType.FLOOD,
                title = "Flood Safety Guidelines",
                subtitle = "Inundation survival and flash flood precautions",
                beforeSteps = listOf(
                    "Know your area's flood risk and evacuation routes to elevated ground.",
                    "Store valuable documents in waterproof containers at the highest floor.",
                    "Clear drains, gutters, and downspouts around your property."
                ),
                duringSteps = listOf(
                    "Turn around, don't drown! Never attempt to drive through flooded roads (just 12 inches of moving water can sweep cars).",
                    "Disconnect main electrical switch and gas valves if instructed to evacuate.",
                    "Avoid touching electrical equipment if wet or standing in water."
                ),
                afterSteps = listOf(
                    "Do not drink tap water until authorities declare municipal water safe.",
                    "Beware of snakes and wildlife that may have taken refuge in buildings.",
                    "Take photos of property damage for insurance and disaster relief claims."
                )
            ),
            SafetyTip(
                id = "tip_cyclone",
                disasterType = DisasterType.CYCLONE,
                title = "Cyclone & Hurricane Protection",
                subtitle = "High wind barriers and storm surge defense",
                beforeSteps = listOf(
                    "Trim dead branches and tree limbs near your house.",
                    "Board up or shutter all large windows and sliding glass doors.",
                    "Keep emergency battery lights, spare batteries, and dry rations ready."
                ),
                duringSteps = listOf(
                    "Stay inside the strongest room without exterior windows (like an interior hallway or bathroom).",
                    "Do not go outside during the eye of the storm; winds will resume suddenly from the opposite direction.",
                    "Disconnect non-essential electrical equipment."
                ),
                afterSteps = listOf(
                    "Wait for official 'all-clear' notification before stepping outdoors.",
                    "Watch out for dangling live electrical wires and unstable damaged trees.",
                    "Report damaged infrastructure to local disaster management helpline (1070)."
                )
            ),
            SafetyTip(
                id = "tip_fire",
                disasterType = DisasterType.FIRE,
                title = "Wildfire & Structural Fire",
                subtitle = "Defensible space and evacuation rules",
                beforeSteps = listOf(
                    "Create a 30-foot defensible perimeter around your home free of dry leaves and brush.",
                    "Ensure smoke alarms are installed on every level and tested monthly.",
                    "Plan at least two evacuation routes out of your neighborhood."
                ),
                duringSteps = listOf(
                    "If smoke fills the room, crawl low under the smoke where air is cleaner.",
                    "Feel door handles before opening; if hot, do not open—use an alternate exit.",
                    "Evacuate immediately without delaying to collect personal belongings."
                ),
                afterSteps = listOf(
                    "Do not re-enter burned areas until fire officials confirm safety.",
                    "Wear N95 masks when sifting through ash to protect against toxic particulates.",
                    "Check the roof and exterior for hidden embers or smoldering debris."
                )
            ),
            SafetyTip(
                id = "tip_lightning",
                disasterType = DisasterType.THUNDERSTORM,
                title = "Thunderstorm & Lightning Safety",
                subtitle = "The 30-30 Rule and surge mitigation",
                beforeSteps = listOf(
                    "Follow the 30-30 rule: If time between lightning flash and thunder is under 30 seconds, go indoors.",
                    "Install surge protectors for vital appliances and computers.",
                    "Postpone outdoor sporting events when thunderstorms are in forecast."
                ),
                duringSteps = listOf(
                    "Stay indoors away from corded phones, electrical sockets, and plumbing pipes.",
                    "Never shelter under isolated trees, metal towers, or open gazebos.",
                    "If caught in an open field, crouch into a ball with minimal ground contact."
                ),
                afterSteps = listOf(
                    "Wait at least 30 minutes after hearing the last clap of thunder before leaving shelter.",
                    "Check on neighbors who may need assistance.",
                    "Report downed power lines immediately to 112 or electric utility."
                )
            ),
            SafetyTip(
                id = "tip_rain",
                disasterType = DisasterType.RAIN,
                title = "Heavy Rain & Urban Inundation",
                subtitle = "Safe transit and drainage safeguards",
                beforeSteps = listOf(
                    "Inspect roof waterproofing and storm drain pipes before monsoon season.",
                    "Keep emergency umbrellas, raincoats, and water-sealed pouches for mobiles.",
                    "Check traffic advisories before commuting in heavy downpours."
                ),
                duringSteps = listOf(
                    "Drive slowly with low-beam headlights; maintain double following distance.",
                    "Avoid underpasses and subway tunnels prone to water accumulation.",
                    "Stay clear of metal poles, transformers, and submerged electrical cables."
                ),
                afterSteps = listOf(
                    "Dry out electrical sockets before turning on main circuit breakers.",
                    "Disinfect waterlogged floors to prevent waterborne infections and dengue.",
                    "Report open manholes and missing drain grates to municipal authorities."
                )
            )
        )
    }

    fun getEmergencyContacts(): List<EmergencyContact> {
        return listOf(
            EmergencyContact("National Emergency Helpline", "112", "All-in-one emergency SOS for Police, Fire, Ambulance", R.drawable.ic_sos),
            EmergencyContact("Disaster Management (NDMA)", "1070", "National Disaster Relief and rescue operations", R.drawable.ic_warning),
            EmergencyContact("Police Control Room", "100", "Immediate law enforcement & security assistance", R.drawable.ic_call),
            EmergencyContact("Medical Ambulance", "108", "Emergency medical technician & ambulance dispatch", R.drawable.ic_call),
            EmergencyContact("Fire & Rescue Service", "101", "Fire control, trapped victim extrication", R.drawable.ic_fire),
            EmergencyContact("Women Helpline", "1091", "Emergency safety helpline for women & children", R.drawable.ic_call)
        )
    }
}
