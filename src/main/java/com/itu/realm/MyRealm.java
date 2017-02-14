package com.itu.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.itu.entity.User;
import com.itu.service.UserService;

/**
 * customized Realm for security
 * @author xu
 *
 */
public class MyRealm extends AuthorizingRealm{
	
	@Resource
	private UserService userService;
	
	
	/**
	 * Ϊ��ǰ��¼���û������ɫ��Ȩ��
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String)principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRole(userService.getByUserName(userName).getRole());
		return authorizationInfo;
	}
	
	/**
	 * ��֤��ǰ��¼���û�
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String)token.getPrincipal();
		User user = userService.getByUserName(userName);
		if(user != null){
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", user);// save currentUser information into session
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), "myRealm");
			return authenticationInfo;
		}else{
			return null;
		}
	}
	
}
