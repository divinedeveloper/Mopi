package com.themopi.achievements


import com.themopi.account.User
import grails.transaction.Transactional

@Transactional
class BadgeService {

    def serviceMethod() {

    }
	def assignBadgeName(user)
	{
		
		
		if(user.profile!=null && user.profile?.points != null)
		{
			long point=user.profile.points 
			if(point>=10 && point<50)
			{
				user.profile.badge="Newbie"
			}
			else if(point>=50 && point<100)
			{
				user.profile.badge="Freshman"
			}
			else if(point>=100 && point<200)
			{
				user.profile.badge="Sophomore"
			}
			else if(point>=200 && point<300)
			{
				user.profile.badge="Junior"
			}
			else if(point>=300 && point<500)
			{
				user.profile.badge="Senior"
			}
			else if(point>=500 && point<650)
			{
				user.profile.badge="Intern"
			}
			else if(point>=650 && point<800)
			{
				user.profile.badge="Clerk"
			}
			else if(point>=800 && point<1000)
			{
				user.profile.badge="Admin"
			}
			else if(point>=1000 && point<1200)
			{
				user.profile.badge="Supervisor"
			}
			else if(point>=1200 && point<1500)
			{
				user.profile.badge="Junior Manager"
			}
			else if(point>=1500 && point<2000)
			{
				user.profile.badge="Manager"
			}
			else if(point>=2000 && point<3000)
			{
				user.profile.badge="Junior Executive"
			}
			else if(point>=3000 && point<5000)
			{
				user.profile.badge="Executive"
			}
			else if(point>=5000 && point<10000)
			{
				user.profile.badge="Director"
			}
			else if(point>=10000)
			{
				user.profile.badge="President"
			}
			
		}
		user.save()
	}
}
