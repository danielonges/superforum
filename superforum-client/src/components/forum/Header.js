import React, { useEffect, useContext, useState } from 'react';
import PropTypes from 'prop-types';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { Link } from 'react-router-dom';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';
import AccountMenu from '../layout/AccountMenu';
import logo from '../../resources/logo/superforum-logo.png';

const Header = (props) => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { user } = userContext;
    const { forumCategories, getAllForumCategories } = forumContext;

    const [searchField, setSearchField] = useState('');

    useEffect(() => {
        getAllForumCategories();
        // eslint-disable-next-line
    }, [JSON.stringify(forumCategories)]);

    const onChange = (e) => {
        setSearchField(e.target.value);
    };

    return (
        <React.Fragment>
            <Toolbar
                sx={{
                    borderBottom: 1,
                    borderColor: 'divider'
                }}
            >
                <Link to='/' align='center' style={{ marginRight: 10 }}>
                    <img src={logo} alt='superforum-logo' height={50} />
                </Link>
                <AccountMenu />
                {user.userType === 'ADMIN' && (
                    <Typography
                        component='h2'
                        variant='subtitle2'
                        color='text.secondary'
                        sx={{ flex: 1, marginLeft: 1 }}
                    >
                        Admin User
                    </Typography>
                )}
                <Typography
                    component='h2'
                    variant='h5'
                    color='inherit'
                    align='center'
                    noWrap
                    sx={{ flex: 1 }}
                ></Typography>

                <IconButton
                    component={Link}
                    to={{
                        pathname: '/search-forums',
                        state: {
                            title: searchField
                        }
                    }}
                >
                    <SearchIcon />
                </IconButton>
                <TextField
                    label='Search forums'
                    variant='standard'
                    InputLabelProps={{
                        shrink: true
                    }}
                    sx={{ margin: 1 }}
                    onChange={onChange}
                />
                <Button
                    component={Link}
                    to={{
                        pathname: '/search-forums',
                        state: {
                            title: searchField
                        }
                    }}
                    variant='outlined'
                    size='small'
                >
                    Search
                </Button>
            </Toolbar>
            <Toolbar
                component='nav'
                variant='dense'
                sx={{
                    justifyContent: 'space-between',
                    overflowX: 'auto',
                    borderBottom: 1,
                    borderColor: 'divider',
                    marginBottom: 2
                }}
            >
                {forumCategories != null &&
                    forumCategories.map((category, i) => (
                        <Link
                            to={{
                                pathname: '/forum-category',
                                state: {
                                    category: category.category
                                }
                            }}
                            style={{ textDecoration: 'none' }}
                        >
                            <Typography
                                color='primary'
                                noWrap
                                key={i}
                                variant='body2'
                                sx={{ p: 1, flexShrink: 0 }}
                            >
                                {category.category}
                            </Typography>
                        </Link>
                    ))}
                {/* can add ability to add category here */}
            </Toolbar>
        </React.Fragment>
    );
};

Header.propTypes = {
    sections: PropTypes.arrayOf(
        PropTypes.shape({
            title: PropTypes.string.isRequired,
            url: PropTypes.string.isRequired
        })
    ).isRequired,
    title: PropTypes.string.isRequired
};

export default Header;
