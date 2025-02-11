import React, { useState } from 'react';
import './LoginRegister.css';
import { FaUser, FaEyeSlash, FaEye, FaEnvelope } from "react-icons/fa";

const LoginRegister = () => {
    const [action, setAction] = useState('');
    const [showPassword, setShowPassword] = useState(false);  // State để ẩn/hiện mật khẩu

    const registerLink = () => {
        setAction('active');
    };

    const loginLink = () => {
        setAction('');
    };

    const togglePassword = () => {
        setShowPassword(!showPassword);
    };

    return (
        <div className={`wrapper ${action}`}>
            {/* Login Form */}
            <div className="form-box login">
                <form action="">
                    <h1>Login</h1>
                    <div className="input-box">
                        <input type="text" placeholder='Username' required />
                        <FaUser className='icon' />
                    </div>
                    <div className="input-box">
                        <input 
                            type={showPassword ? "text" : "password"} 
                            placeholder='Password' 
                            required 
                        />
                        {showPassword ? 
                            <FaEye className='icon' onClick={togglePassword} /> 
                            : <FaEyeSlash className='icon' onClick={togglePassword} />
                        }
                    </div>

                    <div className="remember-forgot">
                        <label><input type="checkbox" />Remember me</label>
                        <a href="#">Forgot password?</a>
                    </div>

                    <button type="submit">Login</button>

                    <div className="register-link">
                        <p>Don't have an account? <a href="#" onClick={registerLink}>Register</a></p>
                    </div>
                </form>
            </div>

            {/* Register Form */}
            <div className="form-box register">
                <form action="">
                    <h1>Register</h1>
                    <div className="input-box">
                        <input type="text" placeholder='Username' required />
                        <FaUser className='icon' />
                    </div>
                    <div className="input-box">
                        <input type="email" placeholder='Email' required />
                        <FaEnvelope className='icon' />
                    </div>
                    <div className="input-box">
                        <input 
                            type={showPassword ? "text" : "password"} 
                            placeholder='Password' 
                            required 
                        />
                        {showPassword ? 
                            <FaEye className='icon' onClick={togglePassword} /> 
                            : <FaEyeSlash className='icon' onClick={togglePassword} />
                        }
                    </div>

                    <div className="remember-forgot">
                        <label><input type="checkbox" />I agree to the terms & conditions</label>
                    </div>

                    <button type="submit">Register</button>

                    <div className="register-link">
                        <p>Already have an account? <a href="#" onClick={loginLink}>Login</a></p>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginRegister;
