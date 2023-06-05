package com.gideonisa.security.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;

   @Column(name="first name")
   private String firstName;

   @Column(name="last name")
   private String lastName;

   @Column(name="email")
   private String email;

   @Column(name="password")
   private String password;

   @Enumerated(EnumType.STRING)
   private Role role;

   /**
    * Returns a list of ROLES assigned to the User
    * @return
    */
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
      return List.of(new SimpleGrantedAuthority(role.name()) );
   }

   @Override
   public String getUsername() {
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'getUsername'");

      // here we are using the user's email as the username
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");

      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
   }

   @Override
   public boolean isCredentialsNonExpired() {
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");

      return true;
   }

   @Override
   public boolean isEnabled() {
      // TODO Auto-generated method stub
      //throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");

      return true;
   }

   @Override
   public String getPassword() {
      // TODO Auto-generated method stub
      return password;
      //throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
   }


}
