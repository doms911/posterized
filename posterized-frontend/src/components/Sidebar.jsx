import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AiOutlineBars } from 'react-icons/ai';
import { IoClose } from 'react-icons/io5';
import { IconContext } from 'react-icons';
import { SidebarData } from './SidebarData';
import './Sidebar.css';
import Cookies from "js-cookie";
const Sidebar = ({ userRole }) => {
  const [error, setError] = useState(null);
  const [sidebar, setSidebar] = useState(false);
  const [adminConferences, setAdminConferences] = useState([]);
  const isPinValid = Cookies.get('isPinValid') === 'true';
  useEffect(() => {
    const fetchData = async () => {
      if (userRole === 'admin') {
      try {
          const response = await fetch('/api/konferencija/prikaziAdminuNazive', {
            credentials: 'include',
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          });
          if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
          }
          const data = await response.json();
          setAdminConferences(data);
      } catch (err) {
        setError(err.response ? err.response.data.message : 'Nepoznata greška');
      }}
    };
    fetchData();
  }, [userRole]);
  const showSidebar = () => setSidebar(!sidebar);
  const filteredSidebarData = SidebarData.filter(item => item.allowedRoles.includes(userRole));
  return (
    <>
      <IconContext.Provider value={{ color: '#fff' }}>
        <div className='navbar'>
          <Link to='#' className='menu-bars'>
            <AiOutlineBars onClick={showSidebar} />
          </Link>
        </div>
        <nav className={sidebar ? 'nav-menu active' : 'nav-menu'}>
          <ul className='nav-menu-items' onClick={showSidebar}>
            <li className='navbar-toggle'>
              <Link to='#' className='menu-bars'>
                <IoClose/>
              </Link>
            </li>
            {filteredSidebarData.filter(item => item.path === '/').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {filteredSidebarData.filter(item => item.path === '/addAdmin').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {filteredSidebarData.filter(item => item.path === '/addConference').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {filteredSidebarData.filter(item => item.path === '/conferenceList').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {filteredSidebarData.filter(item => item.path === '/addSponsor').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {filteredSidebarData.filter(item => item.path === '/addAuthor').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {userRole === 'admin' && adminConferences.length !== 0 && (
                <li className='nav-menu-items'>

                  {adminConferences.map((adminConference, index) => (
                      <div className='nav-text' key={index}>
                        <Link to={`/konferencija/${adminConference}`} className='menu-bars'>
                          <span>{adminConference}</span>
                        </Link>
                      </div>
                  ))}
                </li>
            )}
            {filteredSidebarData.filter(item => item.path === '/pinInput').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {isPinValid && filteredSidebarData.filter(item => item.path === '/posters').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {isPinValid && filteredSidebarData.filter(item => item.path === '/videoStream').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
            {isPinValid && filteredSidebarData.filter(item => item.path === '/pictures').map((item, index) => (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    <span>{item.title}</span>
                  </Link>
                </li>
            ))}
          </ul>
        </nav>
      </IconContext.Provider>
    </>
  );
};
export default Sidebar;