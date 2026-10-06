package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FooterStyle
import com.example.model.UserProfile
import com.example.ui.components.AutoFooterComposable
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
  currentProfile: UserProfile,
  onSaveProfile: (UserProfile) -> Unit,
  onBack: () -> Unit
) {
  BackHandler { onBack() }

  var businessName by remember { mutableStateOf(currentProfile.businessName) }
  var ownerName by remember { mutableStateOf(currentProfile.ownerName) }
  var designation by remember { mutableStateOf(currentProfile.designation) }
  var phone by remember { mutableStateOf(currentProfile.phone) }
  var email by remember { mutableStateOf(currentProfile.email) }
  var website by remember { mutableStateOf(currentProfile.website) }
  var address by remember { mutableStateOf(currentProfile.address) }
  var tagLine by remember { mutableStateOf(currentProfile.tagLine) }
  var selectedPreviewStyle by remember { mutableStateOf(FooterStyle.CLASSIC_ROYAL) }

  val previewProfile = currentProfile.copy(
    businessName = businessName,
    ownerName = ownerName,
    designation = designation,
    phone = phone,
    email = email,
    website = website,
    address = address,
    tagLine = tagLine
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Personal Branding (AdBanao Style)",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF131127))
      )
    },
    containerColor = Color(0xFF0F0C20)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Live Auto-Footer Preview
      Text(
        text = "⚡ Real-Time Auto-Footer Preview",
        color = GoldAccent,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "This branding bar is automatically stamped onto every festival & business template you select.",
        color = Color(0xFF94A3B8),
        fontSize = 11.5.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      AutoFooterComposable(
        profile = previewProfile,
        style = selectedPreviewStyle,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Footer style selector chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        FooterStyle.entries.forEach { style ->
          val isSelected = style == selectedPreviewStyle
          Box(
            modifier = Modifier
              .weight(1f)
              .background(
                if (isSelected) SaffronPrimary else Color(0xFF1E1A38),
                RoundedCornerShape(8.dp)
              )
              .border(1.dp, if (isSelected) GoldAccent else Color(0xFF38325E), RoundedCornerShape(8.dp))
              .clickable { selectedPreviewStyle = style }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = style.displayName.split(" ").first(),
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Logo and Photo Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .border(2.dp, GoldAccent, CircleShape)
              .padding(3.dp)
              .clip(CircleShape)
              .background(Color(0xFF1E1A38))
          ) {
            currentProfile.photoRes?.let {
              Image(
                painter = painterResource(id = it),
                contentDescription = "Profile Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Owner Photo", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
          Text("(Transparent PNG)", fontSize = 10.sp, color = EmeraldGreen)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .border(2.dp, SaffronPrimary, RoundedCornerShape(12.dp))
              .padding(3.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF1E1A38))
          ) {
            currentProfile.logoRes?.let {
              Image(
                painter = painterResource(id = it),
                contentDescription = "Business Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Business Logo", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
          Text("(Vector / HD)", fontSize = 10.sp, color = EmeraldGreen)
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Form Fields
      CustomBrandingField(
        label = "Business / Enterprise Name",
        value = businessName,
        onValueChange = { businessName = it },
        icon = Icons.Default.Business,
        testTag = "input_business_name"
      )

      CustomBrandingField(
        label = "Owner / Personal Name",
        value = ownerName,
        onValueChange = { ownerName = it },
        icon = Icons.Default.Person,
        testTag = "input_owner_name"
      )

      CustomBrandingField(
        label = "Designation / Role",
        value = designation,
        onValueChange = { designation = it },
        icon = Icons.Default.Badge,
        testTag = "input_designation"
      )

      CustomBrandingField(
        label = "Contact Number / WhatsApp",
        value = phone,
        onValueChange = { phone = it },
        icon = Icons.Default.Phone,
        testTag = "input_phone"
      )

      CustomBrandingField(
        label = "Email Address",
        value = email,
        onValueChange = { email = it },
        icon = Icons.Default.Email,
        testTag = "input_email"
      )

      CustomBrandingField(
        label = "Website / Social Handle",
        value = website,
        onValueChange = { website = it },
        icon = Icons.Default.Language,
        testTag = "input_website"
      )

      CustomBrandingField(
        label = "Shop / Office Address",
        value = address,
        onValueChange = { address = it },
        icon = Icons.Default.LocationOn,
        testTag = "input_address"
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          onSaveProfile(previewProfile)
          onBack()
        },
        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("save_profile_button")
      ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Save & Apply Auto-Branding",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun CustomBrandingField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  testTag: String
) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      label = { Text(label, fontSize = 12.sp) },
      leadingIcon = { Icon(icon, contentDescription = null, tint = SaffronPrimary) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SaffronPrimary,
        unfocusedBorderColor = Color(0xFF38325E),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = Color(0xFF131127),
        unfocusedContainerColor = Color(0xFF131127)
      ),
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag(testTag),
      singleLine = true
    )
  }
}
