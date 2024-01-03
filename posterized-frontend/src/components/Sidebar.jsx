// Sidebar.jsx

import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { AiOutlineBars } from 'react-icons/ai';
import { IoClose } from 'react-icons/io5';
import { IconContext } from 'react-icons';
import { SidebarData } from './SidebarData';
import './Sidebar.css';

const Sidebar = ({ userRole }) => {
   // console.log('userRole in Sidebar:', userRole);
  const [sidebar, setSidebar] = useState(false);

  const showSidebar = () => setSidebar(!sidebar);

  const filteredSidebarData = SidebarData.filter(item => {
    // Filtrira stavke bočne trake prema dopuštenim ulogama
    return item.allowedRoles.includes(userRole);
  });

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
                <IoClose />
              </Link>
            </li>
            {filteredSidebarData.map((item, index) => {
              return (
                <li key={index} className={item.cName}>
                  <Link to={item.path}>
                    {item.title}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>
      </IconContext.Provider>
    </>
  );
};

export default Sidebar;
