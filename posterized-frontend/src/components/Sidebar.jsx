import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AiOutlineBars } from 'react-icons/ai';
import { IoClose } from 'react-icons/io5';
import { IconContext } from 'react-icons';
import { SidebarData } from './SidebarData';
import './Sidebar.css';
import AdminConference from './AdminConference';

const Sidebar = ({ userRole }) => {
  const [error, setError] = useState(null);
  const [sidebar, setSidebar] = useState(false);
  const [adminConferences, setAdminConferences] = useState([]);

  React.useEffect(() => {
    console.log(userRole)
    fetch('/api/konferencija/prikaziAdminuNazive')
      .then((data) => data.json())
      .then((adminConferences) => setAdminConferences(adminConferences))
      .catch((err) => {
        setError(err.response ? err.response.data.message : 'Nepoznata greška');
      });
      console.log(adminConferences)
  }, []);
  
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
            {filteredSidebarData.map((item, index) => (
              <li key={index} className={item.cName}>
                <Link to={item.path}>
                  {item.title}
                </Link>
              </li>
            ))}
            {userRole === "admin" && (
  <li className='admin-conferences'>
    
    <ul>
      {Array.isArray(adminConferences) ? (
        adminConferences.map((adminConference, index) => (
          <AdminConference key={index} adminConference={adminConference} />
        ))
      ) : (
        <p>Nema</p>
      )}
    </ul>
  </li>
)}

       
          </ul>
        </nav>
      </IconContext.Provider>
    </>
  );
};

export default Sidebar;

